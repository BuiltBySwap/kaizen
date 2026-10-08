package dev.builtbyswap

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
        get("/health") {
            call.respond(Health("ok"))
        }
        get("/plan/today") {
            call.respond(PlanDayDto("2026-10-08", "BE-02", "Backend (Ktor)", "Ktor fundamentals", 1.17))
        }
    }
}