package com.supcis.kaij.hackday_building_management

import io.github.flaxoos.ktor.server.plugins.ratelimiter.*
import io.github.flaxoos.ktor.server.plugins.ratelimiter.implementations.*
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureHTTP() {
    install(Compression)
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    //install(ForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
    //install(XForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
    routing {
        openAPI(path = "openapi", swaggerFile = "openapi/facility.yaml")
    }

    install(RateLimiting) {
        rateLimiter {
            type = TokenBucket::class
            capacity = 100
            rate = 10.seconds
        }
    }
}
