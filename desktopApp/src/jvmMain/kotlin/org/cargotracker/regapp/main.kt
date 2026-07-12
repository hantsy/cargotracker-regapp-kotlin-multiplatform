package org.cargotracker.regapp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Cargotracker Registry",
        state = rememberWindowState(width = 1024.dp, height = 768.dp),
    ) {
        App()
    }
}
