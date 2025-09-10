package com.example.digitalclusterapp.feature.cluster.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.digitalclusterapp.core.designsystem.theme.LocalClusterTypography

/**
 * Displays the current engine RPM with a half-circle gauge.
 *
 * @param rpm Current RPM value
 * @param modifier Modifier for the component
 * @param maxRpmK Maximum RPM value in thousands for gauge scaling
 */
@Composable
fun ClusterRpmDisplay(
    modifier: Modifier = Modifier,
    rpm: Int,
    maxRpmK: Float = 8f
) {
    val typography = LocalClusterTypography.current

    Box(modifier = modifier) {
        BoxWithConstraints {
            val gaugeSize = maxHeight * 0.95f

            val rpmValue by animateFloatAsState(
                targetValue = rpm.toFloat(),
                label = "rpm"
            )

            val progress = (rpmValue / (maxRpmK * 1000)).coerceIn(0f, 1f)

            Box(Modifier.size(gaugeSize)) {
                ClusterRingCircle(
                    value = progress,
                    side = HalfSide.RPM,
                    thickness = 40.dp,
                    endExtension = 150.dp,
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (rpmValue / 1000).toInt().toString().padStart(2, '0'),
                            style = typography.mainSpeedRpm
                        )
                        Text(
                            text = "rpm x 1000",
                            style = typography.subSpeedRpm
                        )
                    }
                }
            }
        }
    }
}