package org.cargotracker.regapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.cargotracker.regapp.client.EventType
import org.cargotracker.regapp.client.HandlingReport
import org.cargotracker.regapp.client.HandlingResponse
import org.cargotracker.regapp.client.submitHandlingReport
import kotlin.time.Clock

class HandlingReportViewModel : ViewModel() {
    private val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    var date by mutableStateOf(now.date)
    var time by mutableStateOf(now.time)
    var trackingId by mutableStateOf("")
    var unLocode by mutableStateOf("")
    var eventType by mutableStateOf<EventType?>(null)
    var voyageNumber by mutableStateOf("")
    var message by mutableStateOf("Please fill all fields")
    var messageColor by mutableStateOf(Color.Gray)
    var isSubmitting by mutableStateOf(false)

    fun updateDate(newDate: LocalDate) {
        date = newDate
    }

    fun updateTime(newTime: LocalTime) {
        time = newTime
    }

    fun updateTrackingId(id: String) {
        trackingId = id
    }

    fun updateUnLocode(locode: String) {
        unLocode = locode
    }

    fun updateEventType(type: EventType?) {
        eventType = type
    }

    fun updateVoyageNumber(number: String) {
        voyageNumber = number
    }

    fun submitReport() {
        val type = eventType

        if (trackingId.isBlank() || unLocode.isBlank() || type == null) {
            message = "Please fill all fields"
            messageColor = Color.Red
            return
        }

        isSubmitting = true
        val report = HandlingReport(
            completionTime = LocalDateTime(date, time),
            trackingId = trackingId,
            eventType = type,
            unLocode = unLocode,
            voyageNumber = voyageNumber.takeIf { it.isNotBlank() }
        )

        viewModelScope.launch {
            submitHandlingReport(report) {
                when (it) {
                    is HandlingResponse.Success -> {
                        message = "Submitted successfully!"
                        messageColor = Color.Green
                    }

                    is HandlingResponse.Error -> {
                        message = it.message
                        messageColor = Color.Red
                    }
                }
                isSubmitting = false
            }
        }
    }
}
