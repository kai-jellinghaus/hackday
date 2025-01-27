package com.supcis.kaij.hackday_building_management

import arrow.core.Either
import arrow.core.getOrNone
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.supcis.kaij.hackday_building_management.models.Facility
import com.supcis.kaij.hackday_building_management.models.GeoLocation
import io.nats.client.Dispatcher
import io.nats.client.JetStream
import io.nats.client.JetStreamManagement
import io.nats.client.Message
import io.nats.client.MessageHandler
import io.nats.client.PushSubscribeOptions
import io.nats.client.api.AckPolicy
import io.nats.client.api.ConsumerConfiguration
import io.nats.client.api.DeliverPolicy
import io.nats.client.api.StreamConfiguration
import io.nats.client.impl.NatsMessage
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.annotation.Single

@JvmInline
@Serializable
value class FacilityId(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)


sealed interface FacilityError
data class FacilityNotFound(val facilityId: FacilityId) : FacilityError

interface FacilityRepo {
    fun get(id: FacilityId): Either<FacilityError, Facility>
    fun set(id: FacilityId, facility: Facility): Either<FacilityError, Unit>
    fun delete(id: FacilityId): Either<FacilityError, Unit>
}

@Single
fun natsFacilityRepoImpl(jetStream: JetStream, dispatcher: Dispatcher): FacilityRepo = NATSFacilityRepoImpl(jetStream, dispatcher)

class NATSFacilityRepoImpl(private val jetStream: JetStream, dispatcher: Dispatcher) : FacilityRepo {
    private val map: MutableMap<FacilityId, Facility> = mutableMapOf()

    init {
        jetStream.subscribe("facility.>", dispatcher, {
            when(val message: FacilityEvent = decodeFromString(it.data.decodeToString().also { println("Received $it") })) {
                is FacilityDataChangedEvent -> {
                    map[message.facilityId] = message.newData
                    println("M1: $map")
                    it.ack()
                }
                is FacilityDeletedEvent -> {
                    map.remove(message.facilityId)
                    println("M2: $map")
                    it.ack()
                }

                else -> {
                    println("M3: $map")
                    it.ack()
                }
            }
        }, false, PushSubscribeOptions.builder()
            .build())
    }

    override fun get(id: FacilityId): Either<FacilityError, Facility> =
        map.getOrNone(id).toEither { FacilityNotFound(id) }

    override fun set(id: FacilityId, facility: Facility): Either<FacilityError, Unit> = either {
        val opt = map.getOrNone(id)
        val isEqual = opt.map { it == facility }.fold({ false }, { it })
        if (!isEqual) {
            jetStream.publish(NatsMessage.builder()
                .subject("facility.${id.toString()}.${FacilityDataChangedEvent.EVENT_TYPE}")
                .data(Json.encodeToString(FacilityDataChangedEvent(id, facility) as FacilityEvent))
                .build())
        }
    }

    override fun delete(id: FacilityId): Either<FacilityError, Unit> = either {
        map.getOrNone(id).toEither { FacilityNotFound(id) }.bind()
        jetStream.publish(NatsMessage.builder()
            .subject("facility.${id.toString()}.${FacilityDeletedEvent.EVENT_TYPE}")
            .data(Json.encodeToString(FacilityDeletedEvent(id) as FacilityEvent))
            .build())
    }


}
