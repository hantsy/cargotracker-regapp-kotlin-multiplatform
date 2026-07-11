package org.cargotracker.regapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import org.cargotracker.regapp.components.Footer
import org.cargotracker.regapp.components.Navbar

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data object Greeting : Route

    @Serializable
    data object HandlingReport : Route
}

@Composable
fun App() {
    val backStack = rememberNavBackStack(SavedStateConfiguration.Companion.DEFAULT, Route.Home)

    val currentRoute = backStack.lastOrNull() as? Route

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
                modifier = Modifier.padding(innerPadding),
                entryProvider = entryProvider {
                    entry<Route.Home> { HomePage() }
                    entry<Route.Greeting> { GreetingPage() }
                    entry<Route.HandlingReport> { HandlingReportView() }
                }
            )
        }
    }
}
