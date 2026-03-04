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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.*
import org.cargotracker.regapp.client.EventType
import org.cargotracker.regapp.components.MaterialIcon
import org.cargotracker.regapp.components.MaterialIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandlingReportView(
    modifier: Modifier = Modifier,
    viewModel: HandlingReportViewModel = viewModel { HandlingReportViewModel() }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        // Form
        Card(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Completion Time
                val formattedDateTime = "${viewModel.date} ${viewModel.time.hour.toString().padStart(2, '0')}:${viewModel.time.minute.toString().padStart(2, '0')}"
                var showDatePicker by remember { mutableStateOf(false) }
                var showTimePicker by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = formattedDateTime,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Completion Time") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = { showDatePicker = true }) {
                                MaterialIcon(MaterialIcons.DateRange, fontSize = 18.sp)
                                Spacer(Modifier.width(4.dp))
                                Text("Date")
                            }
                            TextButton(onClick = { showTimePicker = true }) {
                                MaterialIcon(MaterialIcons.AccessTime, fontSize = 18.sp)
                                Spacer(Modifier.width(4.dp))
                                Text("Time")
                            }
                        }
                    }
                )

                if (showDatePicker) {
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = viewModel.date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
                    )
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    viewModel.updateDate(
                                        kotlin.time.Instant.fromEpochMilliseconds(it)
                                            .toLocalDateTime(TimeZone.UTC).date
                                    )
                                }
                                showDatePicker = false
                            }) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                if (showTimePicker) {
                    val timePickerState = rememberTimePickerState(
                        initialHour = viewModel.time.hour,
                        initialMinute = viewModel.time.minute
                    )
                    AlertDialog(
                        onDismissRequest = { showTimePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.updateTime(LocalTime(timePickerState.hour, timePickerState.minute))
                                showTimePicker = false
                            }) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
                        },
                        text = {
                            TimePicker(state = timePickerState)
                        }
                    )
                }

                // Tracking Id
                OutlinedTextField(
                    value = viewModel.trackingId,
                    onValueChange = { viewModel.updateTrackingId(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Tracking Id") },
                    singleLine = true
                )

                // Location
                OutlinedTextField(
                    value = viewModel.unLocode,
                    onValueChange = { viewModel.updateUnLocode(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Location") },
                    singleLine = true
                )

                // Event Type
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = viewModel.eventType?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        label = { Text("Select Event Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        EventType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    viewModel.updateEventType(type)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Voyage Number
                OutlinedTextField(
                    value = viewModel.voyageNumber,
                    onValueChange = { viewModel.updateVoyageNumber(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Voyage Number") },
                    singleLine = true
                )

                // Submit Button
                Button(
                    onClick = { viewModel.submitReport() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewModel.isSubmitting
                ) {
                    Text("Submit Report")
                }

                // Message
                Text(
                    viewModel.message,
                    color = viewModel.messageColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
