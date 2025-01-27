package com.supcis.kaij.hackday_building_management

import com.supcis.kaij.hackday_building_management.controllers.FacilityController
import com.supcis.kaij.hackday_building_management.controllers.TypedApplicationCall
import com.supcis.kaij.hackday_building_management.models.Facility
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import java.lang.IllegalStateException
import java.util.UUID
import org.koin.core.annotation.Single

@Single
fun buildFacilityController(facilityRepo: FacilityRepo) = FacilityControllerImpl(facilityRepo)

class FacilityControllerImpl(private val facilityRepo: FacilityRepo) : FacilityController {
    override suspend fun getById(facilityId: String, call: TypedApplicationCall<Facility>) {
        val id: FacilityId
        try {
            id = FacilityId(UUID.fromString(facilityId))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
            return
        }

        facilityRepo.get(id)
            .onLeft { e -> when(e) {
                is FacilityNotFound -> call.respond(HttpStatusCode.NotFound)
            } }
            .onRight { facility -> call.respondTyped(HttpStatusCode.OK, facility) }
    }

    override suspend fun putById(facilityId: String, facility: Facility, call: ApplicationCall) {
        val id: FacilityId
        try {
            id = FacilityId(UUID.fromString(facilityId))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
            return
        }

        val exists = facilityRepo.get(id)
        facilityRepo.set(id, facility)
        .onLeft { e -> when(e) {
            is FacilityNotFound -> throw IllegalStateException("When the facility does not exist, it should be created.")
        } }
        .onRight { exists.onLeft { call.respond(HttpStatusCode.Created) }.onRight { call.respond(HttpStatusCode.NoContent) }}
    }

    override suspend fun deleteById(facilityId: String, call: ApplicationCall) {
        val id: FacilityId
        try {
            id = FacilityId(UUID.fromString(facilityId))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
            return
        }

        facilityRepo.delete(id)
            .onLeft { e -> when(e) {
                is FacilityNotFound -> call.respond(HttpStatusCode.NotFound)
            } }
            .onRight { call.respond(HttpStatusCode.NoContent) }
    }
}
