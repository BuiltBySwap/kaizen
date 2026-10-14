package dev.builtbyswap

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate
import java.time.ZoneId
import io.ktor.http.HttpHeaders
import io.ktor.server.request.receive
import io.ktor.http.ContentType
import io.ktor.server.request.contentType

private val IST = ZoneId.of("Asia/Kolkata")

fun Application.configureRouting() {
    val repository = PlanRepository.fromResource("/seed/plan.csv")   // read ONCE, when the server starts
    val progressStore = ProgressStore()

    routing {
        get("/health") {
            call.respond(Health("ok"))
        }

        get("/plan/today") {
            val today = LocalDate.now(IST).toString()
            val day = repository.forDate(today)
            if (day == null) call.respond(HttpStatusCode.NotFound, ApiError("no plan for $today"))
            else call.respond(day)
        }

        get("/plan/{date}") {
            val text = call.parameters["date"].orEmpty()
            val date = runCatching { LocalDate.parse(text) }.getOrNull()
            if (date == null) {
                call.respond(HttpStatusCode.BadRequest, ApiError("date must look like 2026-10-11"))
                return@get
            }
            val day = repository.forDate(date.toString())
            if (day == null) call.respond(HttpStatusCode.NotFound, ApiError("no plan for $date"))
            else call.respond(day)
        }

        get("/questions/{n}") {
            val number = call.parameters["n"]?.toIntOrNull()
            if (number == null) {
                call.respond(HttpStatusCode.BadRequest, ApiError("n must be a number"))
                return@get
            }
            val question = repository.question(number)
            if (question == null) call.respond(HttpStatusCode.NotFound, ApiError("no question $number"))
            else call.respond(question)
        }
        post("/progress") {
            if (call.rejectIfNotJson()) return@post          // the 415 case, decided by us
            val request = call.receive<ProgressRequest>()     // JSON -> object. The validation rules run HERE
            val created = progressStore.create(request)
            if (created == null) {
                call.respond(HttpStatusCode.Conflict, ApiError("progress for ${request.date} already exists", "conflict"))
                return@post
            }
            call.response.header(HttpHeaders.Location, "/progress/${created.date}")   // where the new thing lives
            call.respond(HttpStatusCode.Created, created)                             // 201 + the object
        }

        get("/progress/{date}") {
            val date = call.parameters["date"].orEmpty()
            val progress = progressStore.find(date)
            if (progress == null) call.respond(HttpStatusCode.NotFound, ApiError("no progress for $date", "not_found"))
            else call.respond(progress)
        }
    }
}

/** Answers 415 and returns true when the request body is not application/json. */
private suspend fun ApplicationCall.rejectIfNotJson(): Boolean {
    if (request.contentType().withoutParameters() == ContentType.Application.Json) return false
    respond(HttpStatusCode.UnsupportedMediaType, ApiError("send the body as application/json", "unsupported_media_type"))
    return true
}