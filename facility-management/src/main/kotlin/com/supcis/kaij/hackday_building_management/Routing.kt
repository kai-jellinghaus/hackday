package com.supcis.kaij.hackday_building_management

import com.supcis.kaij.hackday_building_management.controllers.FacilityController.Companion.facilityRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val service by inject<FacilityControllerImpl>()

    routing {
        facilityRoutes(service)
    }
}
