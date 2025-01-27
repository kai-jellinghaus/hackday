package com.supcis.kaij.hackday_building_management

import com.supcis.kaij.hackday_building_management.models.Facility
import io.nats.client.Connection
import io.nats.client.Dispatcher
import io.nats.client.JetStream
import io.nats.client.JetStreamManagement
import io.nats.client.Nats
import io.nats.client.api.StreamConfiguration
import java.util.UUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.koin.core.annotation.Single

@Single
fun nats(): Connection = Nats.connect()

@Single
fun jetStream(nats: Connection): JetStream = nats.jetStream()

@Single
fun jetStreamManagement(nats: Connection): JetStreamManagement = nats.jetStreamManagement()

@Single
fun dispatcher(nats: Connection): Dispatcher = nats.createDispatcher()

@Serializable
sealed class FacilityEvent

@Serializable
@SerialName(FacilityDataChangedEvent.EVENT_TYPE)
data class FacilityDataChangedEvent(val facilityId: FacilityId, val newData: Facility) : FacilityEvent() {
    companion object {
        public const val EVENT_TYPE = "data-update"
    }
}

@Serializable
@SerialName(FacilityDeletedEvent.EVENT_TYPE)
data class FacilityDeletedEvent(val facilityId: FacilityId) : FacilityEvent() {
    companion object {
        public const val EVENT_TYPE = "delete"
    }
}

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}
