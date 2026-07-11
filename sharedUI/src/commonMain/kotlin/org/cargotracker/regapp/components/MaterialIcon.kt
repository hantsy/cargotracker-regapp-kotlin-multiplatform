package org.cargotracker.regapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cargotrackerregappkmp.sharedui.generated.resources.Res
import cargotrackerregappkmp.sharedui.generated.resources.ic_date_range_24px
import cargotrackerregappkmp.sharedui.generated.resources.ic_github_24px
import cargotrackerregappkmp.sharedui.generated.resources.ic_linkedin_24px
import cargotrackerregappkmp.sharedui.generated.resources.ic_schedule_24px
import cargotrackerregappkmp.sharedui.generated.resources.ic_twitter_24px
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MaterialIcon(
    icon: DrawableResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = Color.Unspecified,
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}

@Composable
fun SocialIcon(
    icon: DrawableResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
) {
    Image(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
    )
}

object MaterialIcons {
    val DateRange = Res.drawable.ic_date_range_24px
    val Schedule = Res.drawable.ic_schedule_24px
}

object SocialIcons {
    val GitHub = Res.drawable.ic_github_24px
    val LinkedIn = Res.drawable.ic_linkedin_24px
    val Twitter = Res.drawable.ic_twitter_24px
}
