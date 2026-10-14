package dev.builtbyswap

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.UnsupportedMediaTypeException
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.SerializationException
import java.time.LocalDate

val ALLOWED_STATUSES = setOf("not-started", "in-progress", "partial", "done", "skipped")

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<ProgressRequest> { request ->
            val problems = mutableListOf<String>()
            if (request.status !in ALLOWED_STATUSES)
                problems += "status: must be one of ${ALLOWED_STATUSES.joinToString()}"
            if (runCatching { LocalDate.parse(request.date) }.isFailure)
                problems += "date: must look like 2026-10-15"
            if (request.note.length > 500)
                problems += "note: must be at most 500 characters"
            if (problems.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(problems)
        }
    }

    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            val fields = cause.reasons.map { FieldError(it.substringBefore(": "), it.substringAfter(": ")) }
            call.respond(HttpStatusCode.UnprocessableEntity, ApiError("validation failed", "validation_failed", fields))
        }
        exception<UnsupportedMediaTypeException> { call, _ ->
            call.respond(HttpStatusCode.UnsupportedMediaType,
                ApiError("send the body as application/json", "unsupported_media_type"))
        }
        exception<Throwable> { call, cause ->
            val chain = generateSequence(cause) { it.cause }
            if (chain.any { it is SerializationException || it is BadRequestException }) {
                call.respond(HttpStatusCode.BadRequest,
                    ApiError("the request body is not valid for this endpoint", "bad_request"))
            } else {
                call.application.environment.log.error("Unhandled error", cause)    // the details stay in OUR log
                call.respond(HttpStatusCode.InternalServerError, ApiError("internal error", "internal_error"))
            }
        }
    }
}