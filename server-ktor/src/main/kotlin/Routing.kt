package dev.builtbyswap

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate
import java.time.ZoneId

private val IST = ZoneId.of("Asia/Kolkata")

fun Application.configureRouting() {
    val repository = PlanRepository.fromResource("/seed/plan.csv")   // read ONCE, when the server starts

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
    }
}