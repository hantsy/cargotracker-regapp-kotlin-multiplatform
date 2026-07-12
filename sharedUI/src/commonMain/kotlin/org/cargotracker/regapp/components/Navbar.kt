package org.cargotracker.regapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import org.cargotracker.regapp.Route
import org.jetbrains.compose.resources.painterResource

private data class NavItem(val route: Route, val label: String)

private val navItems = listOf(
    NavItem(Route.Home, "Home"),
    NavItem(Route.Greeting, "Greeting"),
    NavItem(Route.HandlingReport, "Handling Report"),
)

private const val NAV_BREAKPOINT = 600

@Composable
fun Navbar(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.15f), Color.Transparent),
                        startY = size.height,
                        endY = size.height + 4.dp.toPx(),
                    ),
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, 4.dp.toPx()),
                )
            },
        color = Color.White.copy(alpha = 0.5f),
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            val isCompact = maxWidth < NAV_BREAKPOINT.dp
            var menuExpanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Logo + title
                Row(
                    modifier = Modifier.clickable { onNavigate(Route.Home) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(48.dp),
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "CargoTracker",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                if (isCompact) {
                    // Hamburger menu for narrow screens
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            MaterialIcon(MaterialIcons.Menu, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                        ) {
                            navItems.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            item.label,
                                            color = if (currentRoute == item.route)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                Color.Unspecified,
                                        )
                                    },
                                    onClick = {
                                        menuExpanded = false
                                        onNavigate(item.route)
                                    },
                                )
                            }
                        }
                    }
                } else {
                    // Full nav bar for wide screens
                    navItems.forEach { item ->
                        TextButton(
                            onClick = { onNavigate(item.route) },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (currentRoute == item.route)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Gray
                            ),
                        ) {
                            Text(item.label)
                        }
                    }
                }
            }
        }
    }
}
