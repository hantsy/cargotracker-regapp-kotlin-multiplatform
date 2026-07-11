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
import org.cargotracker.regapp.client.HandlingReportClient
import org.cargotracker.regapp.client.HandlingResponse
import kotlin.time.Clock

data class HandlingReportUiState(
    val date: LocalDate,
    val time: LocalTime,
    val trackingId: String = "",
    val unLocode: String = "",
    val eventType: EventType? = null,
    val voyageNumber: String = "",
    val message: String = "Please fill all fields",
    val messageColor: Color = Color.Gray,
    val isSubmitting: Boolean = false,
) {
    companion object {
        fun create(clock: Clock = Clock.System) = clock.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .let { now ->
                HandlingReportUiState(
                    date = now.date,
                    time = now.time,
                )
            }
    }
}

class HandlingReportViewModel(
    private val clock: Clock = Clock.System,
) : ViewModel() {
    var uiState by mutableStateOf(HandlingReportUiState.create(clock))
        private set

    fun updateDate(date: LocalDate) {
        uiState = uiState.copy(date = date)
    }

    fun updateTime(time: LocalTime) {
        uiState = uiState.copy(time = time)
    }

    fun updateTrackingId(id: String) {
        uiState = uiState.copy(trackingId = id)
    }

    fun updateUnLocode(locode: String) {
        uiState = uiState.copy(unLocode = locode)
    }

    fun updateEventType(type: EventType?) {
        uiState = uiState.copy(eventType = type)
    }

    fun updateVoyageNumber(number: String) {
        uiState = uiState.copy(voyageNumber = number)
    }

    fun submitReport() {
        val state = uiState
        val type = state.eventType

        if (state.trackingId.isBlank() || state.unLocode.isBlank() || type == null) {
            uiState = state.copy(message = "Please fill all fields", messageColor = Color.Red)
            return
        }

        uiState = state.copy(isSubmitting = true)

        val report = HandlingReport(
            completionTime = LocalDateTime(state.date, state.time),
            trackingId = state.trackingId,
            eventType = type,
            unLocode = state.unLocode,
            voyageNumber = state.voyageNumber.takeIf { it.isNotBlank() }
        )

        viewModelScope.launch {
            val response = HandlingReportClient.create().submitReport(report)
            uiState = when (response) {
                is HandlingResponse.Success -> uiState.copy(
                    message = "Submitted successfully!",
                    messageColor = Color.Green,
                    isSubmitting = false,
                )
                is HandlingResponse.Error -> uiState.copy(
                    message = response.message,
                    messageColor = Color.Red,
                    isSubmitting = false,
                )
            }
        }
    }
}
