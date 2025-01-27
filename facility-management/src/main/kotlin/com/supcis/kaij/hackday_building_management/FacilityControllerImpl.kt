package com.supcis.kaij.hackday_building_management

import com.supcis.kaij.hackday_building_management.controllers.FacilityController
import com.supcis.kaij.hackday_building_management.controllers.TypedApplicationCall
import com.supcis.kaij.hackday_building_management.models.Facility
import io.ktor.server.application.ApplicationCall
import org.koin.core.annotation.Single

@Single
fun buildFacilityController() = FacilityControllerImpl()

class FacilityControllerImpl : FacilityController {
    override suspend fun getById(facilityId: String, call: TypedApplicationCall<Facility>) {

    }

    override suspend fun putById(facilityId: String, facility: Facility, call: ApplicationCall) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(facilityId: String, call: ApplicationCall) {
        TODO("Not yet implemented")
    }
}
