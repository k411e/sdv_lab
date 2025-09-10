package com.example.digitalclusterapp.feature.cluster.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.digitalclusterapp.core.designsystem.theme.ClusterColors
import com.example.digitalclusterapp.core.designsystem.theme.LocalClusterTypography

/**
 * Displays the bottom status bar with fuel and temperature gauges.
 *
 * @param economy Fuel economy text
 * @param temperature Temperature text
 * @param modifier Modifier for the component
 */
@Composable
fun ClusterBottomStatusBar(
    modifier: Modifier = Modifier,
    economy: String,
    temperature: String
) {
    val typography = LocalClusterTypography.current

    Row(
        modifier = modifier
            .width(800.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Text
        Text(
            text = economy,
            style = typography.mainTempGas
        )

        // Center Gauges (side by side)
        Row(
            Modifier.width(665.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FuelGaugeBar()
            Spacer(modifier = Modifier.width(100.dp))
            TemperatureGaugeBar()
        }

        // Right Text
        Text(
            text = temperature,
            style = typography.mainTempGas,
            textAlign = TextAlign.End
        )
    }
}

/**
 * Displays a fuel level gauge bar.
 */
@Composable
fun FuelGaugeBar() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Default.Email,
            contentDescription = "Fuel",
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .width(270.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Red, Color.Green, ClusterColors.ClusterInfoBlue)
                    )
                )
        )
    }
}

/**
 * Displays a temperature gauge bar.
 */
@Composable
fun TemperatureGaugeBar() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Temperature",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )

        Box(
            modifier = Modifier
                .width(273.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Green, Color.Red)
                    )
                )
        )
    }
}