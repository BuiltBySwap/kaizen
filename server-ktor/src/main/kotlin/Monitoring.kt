package dev.builtbyswap

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {   // an EXTENSION function: inside it, `this` is the Application
    install(CallLogging) {                // add the logging plugin to this application
        level = Level.INFO                // the config block: log requests at INFO level
    }
}