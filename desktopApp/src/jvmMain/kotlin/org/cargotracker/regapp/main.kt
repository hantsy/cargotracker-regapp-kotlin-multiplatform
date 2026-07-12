package org.cargotracker.regapp

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.skia.Image

fun main() = application {
    val iconStream = Thread.currentThread().contextClassLoader.getResourceAsStream("logo.png")
    val iconBitmap = iconStream?.use { stream ->
        Image.makeFromEncoded(stream.readBytes()).toComposeImageBitmap()
    }
    val iconPainter = iconBitmap?.let { BitmapPainter(it) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Cargotracker Registry",
        icon = iconPainter,
    ) {
        App()
    }
}
