package com.example.digitalclusterapp.feature.cluster.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.digitalclusterapp.R

/**
 * Data class representing a status indicator in the top bar.
 *
 * @property drawableRes Resource ID for the indicator icon
 * @property tint Color tint to apply to the icon
 * @property active Whether the indicator is active
 */
data class ClusterIndicator(
    @DrawableRes val drawableRes: Int,
    val tint: Color = Color.White,
    val active: Boolean = false,
    val visible: Boolean = false
)

/**
 * Displays a row of status indicators in the top bar.
 *
 * @param indicators List of indicators to display
 * @param modifier Modifier for the component
 * @param iconSpacing Spacing between icons
 */
@Composable
fun ClusterTopBar(
    modifier: Modifier = Modifier,
    iconSpacing: Dp = 25.dp,
    cruiseControl: Boolean = false
) {

    val indicators: List<ClusterIndicator> = defaultIndicators(cruiseControl)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(45.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.top_icon_bar),
            contentDescription = "Background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(iconSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            indicators.forEach { indicator ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(3.dp))
                ) {
                        if (indicator.visible) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = indicator.drawableRes),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

/**
 * Returns the default set of cluster indicators.
 */
@Composable
fun defaultIndicators(cruiseControl: Boolean): List<ClusterIndicator> {
    return listOf(
        ClusterIndicator(drawableRes = R.drawable.ic_left_arror, active = false),
        ClusterIndicator(drawableRes = R.drawable.ic_engine, tint = Color.Yellow),
        ClusterIndicator(drawableRes = if (cruiseControl) R.drawable.ic_cruise_control_active else R.drawable.ic_cruise_control_default, visible = true),
        ClusterIndicator(
            drawableRes = R.drawable.ic_battery,
            tint = Color(0x0062B1FF),
            active = true
        ),
        ClusterIndicator(drawableRes = R.drawable.ic_oil_pressure, tint = Color.Red),
        ClusterIndicator(
            drawableRes = R.drawable.ic_lights,
            tint = Color(0x000A203D),
            active = true
        ),
        ClusterIndicator(
            drawableRes = R.drawable.ic_max_lights,
            tint = Color(0xFF0A203D),
            active = true,
        ),
        ClusterIndicator(drawableRes = R.drawable.ic_low_beam),
        ClusterIndicator(drawableRes = R.drawable.ic_seatbelt),
        ClusterIndicator(
            drawableRes = R.drawable.ic_brake,
            tint = Color(0xFF0A203D),
            active = true
        ),
        ClusterIndicator(drawableRes = R.drawable.ic_right_arror)
    )
}