package org.cargotracker.regapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import org.cargotracker.regapp.components.MaterialIcons
import org.cargotracker.regapp.components.MaterialIcon

@Composable
fun Footer() {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
    ) {
        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            MaterialIcon(
                icon = MaterialIcons.GitHub,
                modifier = Modifier
                    .clickable {
                        uriHandler.openUri("https://github.com/hantsy/cargotracker-regapp-kotlin-multiplatform/")
                    }
            )
            MaterialIcon(
                icon = MaterialIcons.LinkedIn,
                modifier = Modifier
                    .clickable {
                        uriHandler.openUri("https://www.linkedin.com/in/hantsy")
                    }
            )
            MaterialIcon(
                icon = MaterialIcons.Twitter,
                modifier = Modifier
                    .clickable {
                        uriHandler.openUri("https://twitter.com/@hantsy")
                    }
            )
        }
    }
}
