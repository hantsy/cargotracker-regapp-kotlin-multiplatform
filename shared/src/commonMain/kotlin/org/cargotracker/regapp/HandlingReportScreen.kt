package org.cargotracker.regapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.cargotracker.regapp.client.EventType
import org.cargotracker.regapp.client.HandlingReport
import org.cargotracker.regapp.client.HandlingResponse
import org.cargotracker.regapp.client.submitHandlingReport
import cargotrackerregappkmp.shared.generated.resources.Res
import cargotrackerregappkmp.shared.generated.resources.g
import cargotrackerregappkmp.shared.generated.resources.logo
import cargotrackerregappkmp.shared.generated.resources.t
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.graphics.Color as ComposeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandlingReportScreen(
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    var completionTime by remember { mutableStateOf("") }
    var trackingId by remember { mutableStateOf("") }
    var unLocode by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var voyageNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Please fill all fields") }
    var messageColor by remember { mutableStateOf(ComposeColor.Gray) }
    var isSubmitting by remember { mutableStateOf(false) }

    val eventTypes = EventType.entries

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComposeColor.White)
            .padding(horizontal = 50.dp, vertical = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(76.dp)
            )
            Spacer(Modifier.width(24.dp))
            Column {
                Text(
                    "CargoTracker",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = ComposeColor.DarkGray
                )
                Text(
                    "Incident Logging Application",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

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
                Text("Completion Time")
                OutlinedTextField(
                    value = completionTime,
                    onValueChange = { completionTime = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("YYYY-MM-DD HH:MM") }
                )

                // Tracking Id
                Text("Tracking Id")
                OutlinedTextField(
                    value = trackingId,
                    onValueChange = { trackingId = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Location
                Text("Location")
                OutlinedTextField(
                    value = unLocode,
                    onValueChange = { unLocode = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Event Type
                Text("Type")
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = eventType,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("Select Event Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        eventTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    eventType = type.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Voyage Number
                Text("Voyage Number")
                OutlinedTextField(
                    value = voyageNumber,
                    onValueChange = { voyageNumber = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Submit Button
                Button(
                    onClick = {
                        isSubmitting = true
                        
                        val ldt = try {
                            val parts = completionTime.split(" ")
                            val dateParts = parts[0].split("-")
                            val timeParts = parts[1].split(":")
                            LocalDateTime(
                                dateParts[0].toInt(),
                                dateParts[1].toInt(),
                                dateParts[2].toInt(),
                                timeParts[0].toInt(),
                                timeParts[1].toInt()
                            )
                        } catch (e: Exception) {
                            null
                        }

                        if (ldt == null || trackingId.isBlank() || eventType.isBlank() || unLocode.isBlank()) {
                            message = "Please fill all fields correctly (YYYY-MM-DD HH:MM)"
                            messageColor = ComposeColor.Red
                            isSubmitting = false
                        } else {
                            val report = HandlingReport(
                                completionTime = ldt,
                                trackingId = trackingId,
                                eventType = EventType.valueOf(eventType),
                                unLocode = unLocode,
                                voyageNumber = voyageNumber.takeIf { it.isNotBlank() }
                            )
                            coroutineScope.launch {
                                submitHandlingReport(report) {
                                    when (this) {
                                        is HandlingResponse.Success -> {
                                            message = "Submitted successfully!"
                                            messageColor = ComposeColor.Green
                                        }
                                        is HandlingResponse.Error -> {
                                            message = this.message
                                            messageColor = ComposeColor.Red
                                        }
                                    }
                                }
                                isSubmitting = false
                            }
                        }
                    },
                    enabled = !isSubmitting
                ) {
                    Text("Submit Report")
                }

                // Message
                Text(
                    message,
                    color = messageColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Footer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(Res.drawable.g),
                contentDescription = "GitHub",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        uriHandler.openUri("https://github.com/hantsy/cargotracker-regapp")
                    }
            )
            Spacer(Modifier.width(8.dp))
            TextButton(
                onClick = { uriHandler.openUri("https://github.com/hantsy/cargotracker-regapp") }
            ) {
                Text(
                    "hantsy/cargotracker-regapp",
                    color = ComposeColor(0xFFC0C0C0)
                )
            }
            Spacer(Modifier.width(16.dp))
            Icon(
                painter = painterResource(Res.drawable.t),
                contentDescription = "Twitter",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        uriHandler.openUri("https://twitter.com/@hantsy")
                    }
            )
            Spacer(Modifier.width(8.dp))
            TextButton(
                onClick = { uriHandler.openUri("https://twitter.com/@hantsy") }
            ) {
                Text(
                    "@hantsy",
                    color = ComposeColor(0xFFC0C0C0)
                )
            }
        }
    }
}
