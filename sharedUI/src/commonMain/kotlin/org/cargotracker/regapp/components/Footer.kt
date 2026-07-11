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
import org.jetbrains.compose.resources.DrawableResource

private const val GITHUB_URL = "https://github.com/hantsy/cargotracker-regapp-kotlin-multiplatform/"
private const val LINKEDIN_URL = "https://www.linkedin.com/in/hantsy"
private const val TWITTER_URL = "https://twitter.com/@hantsy"

private data class SocialLink(
    val icon: DrawableResource,
    val contentDescription: String,
    val url: String,
)

private val socialLinks = listOf(
    SocialLink(SocialIcons.GitHub, "GitHub", GITHUB_URL),
    SocialLink(SocialIcons.LinkedIn, "LinkedIn", LINKEDIN_URL),
    SocialLink(SocialIcons.Twitter, "Twitter", TWITTER_URL),
)

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
            socialLinks.forEach { link ->
                SocialIcon(
                    icon = link.icon,
                    contentDescription = link.contentDescription,
                    modifier = Modifier.clickable { uriHandler.openUri(link.url) }
                )
            }
        }
    }
}
