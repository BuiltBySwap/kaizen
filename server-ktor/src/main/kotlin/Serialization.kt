package dev.builtbyswap

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {          // the plugin that converts objects <-> JSON
        json(Json {                        // use kotlinx.serialization's JSON format
            ignoreUnknownKeys = true       // tolerant reader: extra fields in a request do not crash us
            encodeDefaults = true          // always write fields that equal their default value
        })
    }
}