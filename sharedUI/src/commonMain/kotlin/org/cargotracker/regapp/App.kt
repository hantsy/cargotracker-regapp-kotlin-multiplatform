package org.cargotracker.regapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberSerializable
import androidx.navigation3.runtime.serialization.SnapshotStateListSerializer
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.entry
import androidx.navigation3.ui.navDisplayEntryProvider
import kotlinx.serialization.Serializable
import org.cargotracker.regapp.components.Footer
import org.cargotracker.regapp.components.Navbar

@Serializable
sealed interface Route {
    @Serializable data object Home : Route
    @Serializable data object Greeting : Route
    @Serializable data object HandlingReport : Route
}

@Composable
fun App() {
    val backStack = rememberSerializable(
        serializer = SnapshotStateListSerializer()
    ) {
        mutableStateListOf<Route>(Route.Home)
    }

    val currentRoute = backStack.lastOrNull()

    MaterialTheme {
        Scaffold(
            topBar = {
                Navbar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        backStack.add(route)
                    }
                )
            },
            bottomBar = { Footer() }
        ) { innerPadding ->
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                modifier = Modifier.padding(innerPadding),
                entryProvider = navDisplayEntryProvider {
                    entry<Route.Home> { HomePage() }
                    entry<Route.Greeting> { GreetingPage() }
                    entry<Route.HandlingReport> { HandlingReportView() }
                }
            )
        }
    }
}
