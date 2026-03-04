package org.cargotracker.regapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cargotrackerregappkmp.sharedui.generated.resources.Res
import cargotrackerregappkmp.sharedui.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun Navbar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.15f), Color.Transparent),
                        startY = size.height,
                        endY = size.height + 4.dp.toPx()
                    ),
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, 4.dp.toPx())
                )
            },
        color = Color.White.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable { onNavigate("home") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "CargoTracker",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
            }
            
            Spacer(Modifier.weight(1f))
            
            TextButton(
                onClick = { onNavigate("home") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (currentRoute == "home") MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text("Home")
            }

            TextButton(
                onClick = { onNavigate("greeting") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (currentRoute == "greeting") MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text("Greeting")
            }
            
            TextButton(
                onClick = { onNavigate("handling-report") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (currentRoute == "handling-report") MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text("Handling Report")
            }
        }
    }
}
