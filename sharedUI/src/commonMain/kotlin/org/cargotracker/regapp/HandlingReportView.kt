package org.cargotracker.regapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.*
import org.cargotracker.regapp.client.EventType
import org.cargotracker.regapp.components.MaterialIcon
import org.cargotracker.regapp.components.MaterialIcons
import kotlin.time.Instant

// ── DateTime picker field ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimePickerField(
    date: LocalDate,
    time: LocalTime,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    val formattedDateTime =
        "$date ${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = formattedDateTime,
        onValueChange = {},
        readOnly = true,
        label = { Text("Completion Time") },
        modifier = modifier.fillMaxWidth(),
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        trailingIcon = {
            Row {
                IconButton(onClick = { showDatePicker = true }) {
                    MaterialIcon(MaterialIcons.DateRange, contentDescription = "Pick date", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { showTimePicker = true }) {
                    MaterialIcon(MaterialIcons.Schedule, contentDescription = "Pick time", tint = MaterialTheme.colorScheme.primary)
                }
            }
        },
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(
                            Instant.fromEpochMilliseconds(it)
                                .toLocalDateTime(TimeZone.UTC).date
                        )
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = time.hour,
            initialMinute = time.minute,
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onTimeSelected(LocalTime(timePickerState.hour, timePickerState.minute))
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = { TimePicker(state = timePickerState) },
        )
    }
}

// ── Event-type dropdown ──────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventTypeDropdown(
    selected: EventType?,
    onSelected: (EventType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = selected?.name ?: "",
            onValueChange = {},
            readOnly = true,
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            label = { Text("Select Event Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            EventType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onSelected(type)
                        expanded = false
                    },
                )
            }
        }
    }
}

// ── Main form view ───────────────────────────────────────────────────────────

@Composable
fun HandlingReportView(
    modifier: Modifier = Modifier,
    viewModel: HandlingReportViewModel = viewModel { HandlingReportViewModel() },
) {
    val state = viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .padding(vertical = 8.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                DateTimePickerField(
                    date = state.date,
                    time = state.time,
                    onDateSelected = viewModel::updateDate,
                    onTimeSelected = viewModel::updateTime,
                )

                OutlinedTextField(
                    value = state.trackingId,
                    onValueChange = viewModel::updateTrackingId,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Tracking Id") },
                    singleLine = true,
                )

                OutlinedTextField(
                    value = state.unLocode,
                    onValueChange = viewModel::updateUnLocode,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Location") },
                    singleLine = true,
                )

                EventTypeDropdown(
                    selected = state.eventType,
                    onSelected = viewModel::updateEventType,
                )

                OutlinedTextField(
                    value = state.voyageNumber,
                    onValueChange = viewModel::updateVoyageNumber,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Voyage Number") },
                    singleLine = true,
                )

                Button(
                    onClick = viewModel::submitReport,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSubmitting,
                ) {
                    Text("Submit Report")
                }

                Text(
                    state.message,
                    color = state.messageColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
