package org.cargotracker.regapp.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import cargotrackerregappkmp.sharedui.generated.resources.Res
import cargotrackerregappkmp.sharedui.generated.resources.material_symbols
import org.jetbrains.compose.resources.Font

@Composable
fun MaterialIconFont(): FontFamily {
    return FontFamily(Font(Res.font.material_symbols))
}

@Composable
fun MaterialIcon(
    icon: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    color: Color = LocalContentColor.current
) {
    Text(
        text = icon,
        fontFamily = MaterialIconFont(),
        fontSize = fontSize,
        color = color,
        modifier = modifier
    )
}

object MaterialIcons {
    const val GitHub = "\ue885" // Using a generic home or code icon as placeholder if specific branding not in font
    const val LinkedIn = "\ue897" 
    const val Twitter = "\ue8d2"
    const val DateRange = "\ue916"
    const val AccessTime = "\ue192"
}
