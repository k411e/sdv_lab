package com.example.digitalclusterapp.feature.cluster.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.digitalclusterapp.R
import com.example.digitalclusterapp.core.designsystem.theme.LocalClusterTypography

/**
 * Displays the current vehicle speed with a half-circle gauge.
 *
 * @param speed Current speed in mph
 * @param modifier Modifier for the component
 * @param maxSpeed Maximum speed value for gauge scaling
 */
@Composable
fun ClusterSpeedDisplay(
    modifier: Modifier = Modifier,
    speed: Int,
    speedUnit: String,
    maxSpeed: Float = 180f
) {
    val typography = LocalClusterTypography.current

    Box(modifier = modifier) {

        val speedValue by animateFloatAsState(
            targetValue = speed.toFloat(),
            label = "speed"
        )

        Box(
            modifier = modifier.size(684.dp, 684.dp), // Adjust size as needed
            contentAlignment = Alignment.Center
        ) {
            // Main image
            Image(
                painter = painterResource(id = R.drawable.ic_speedometer_gauge),
                contentDescription = "Gauge Base",
                modifier = Modifier.fillMaxSize()
            )

            // Ellipse gradient
            val rotationAngle = ((speed.toFloat() / maxSpeed) * 213f)
            Image(
                painter = painterResource(id = R.drawable.ic_speedometer_ellipse),
                contentDescription = "Gauge Gradient",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationZ = rotationAngle
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                    }
            )

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = speedValue.toInt().toString(),
                    style = typography.mainSpeedRpm
                )
                Text(
                    text = speedUnit,
                    style = typography.subSpeedRpm
                )
            }
        }
    }
}