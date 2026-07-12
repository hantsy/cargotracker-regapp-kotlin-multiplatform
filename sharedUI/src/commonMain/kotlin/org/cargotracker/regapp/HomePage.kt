package org.cargotracker.regapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cargotrackerregappkmp.sharedui.generated.resources.Res
import cargotrackerregappkmp.sharedui.generated.resources.cargo_bg
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomePage() =
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(Res.drawable.cargo_bg),
                contentScale = ContentScale.FillBounds
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Title block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF1A3A5C).copy(alpha = 0.80f),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Welcome to CargoTracker",
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }

            // Description block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Black.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "A Kotlin Multiplatform application for submitting cargo handling " +
                        "reports to the CargoTracker core system. " +
                        "Built with Compose Multiplatform, targeting Android, iOS, desktop, and web.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.90f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
