
plugins {
    alias(libs.plugins.kotlin.jvm)                      // compile Kotlin to a JVM program
    alias(libs.plugins.kotlin.serialization)     // the compiler plugin that makes @Serializable work
    alias(ktorLibs.plugins.ktor)                            // Ktor's Gradle plugin (packaging, run tasks)
}

group = "dev.builtbyswap"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.core)                // Application, call, routing
    implementation(ktorLibs.server.netty)               // the engine: sockets and threads (BE-01)
    implementation(ktorLibs.server.contentNegotiation)  // object <-> JSON conversion
    implementation(ktorLibs.serialization.kotlinx.json) // the JSON format used with it
    implementation(ktorLibs.server.callLogging)         // request logging
    implementation(ktorLibs.server.requestValidation)   // rule checks on request bodies
    implementation(ktorLibs.server.statusPages)         // exception -> response
    implementation(libs.logback.classic)                // the logging backend

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
