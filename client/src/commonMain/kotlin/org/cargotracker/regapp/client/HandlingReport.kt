package org.cargotracker.regapp.client

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
enum class EventType {
    LOAD, UNLOAD, RECEIVE, CLAIM, CUSTOMS
}

// custom format '2023-07-01 12:35'
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val format = LocalDateTime.Format {
        date(LocalDate.Formats.ISO)
        char(' ')
        hour(); char(':'); minute()
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(format))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), format)
    }
}

@Serializable
data class HandlingReport(
    @Serializable(with = LocalDateTimeSerializer::class)
    val completionTime: LocalDateTime,
    val trackingId: String,
    val eventType: EventType,
    val unLocode: String,
    val voyageNumber: String? = null,
)
