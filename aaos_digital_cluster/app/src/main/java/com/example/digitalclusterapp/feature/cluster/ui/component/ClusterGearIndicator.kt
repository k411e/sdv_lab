package com.example.digitalclusterapp.feature.cluster.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.digitalclusterapp.core.designsystem.theme.ClusterColors
import com.example.digitalclusterapp.core.designsystem.theme.LocalClusterTypography

/**
 * Displays the current transmission gear selection.
 *
 * @param currentGear Current selected gear (P, R, N, D)
 * @param modifier Modifier for the component
 */
@Composable
fun ClusterGearIndicator(
    modifier: Modifier = Modifier,
    currentGear: Char
) {
    val typography = LocalClusterTypography.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        listOf('P', 'N', 'D', 'R').forEach { gear ->
            val active = gear == currentGear

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = gear.toString(),
                    style = if (active) typography.mainGear else typography.subGear
                )

                if (active) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(3.dp)
                            .background(ClusterColors.ClusterInfoBlue)
                    )
                } else {
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }
        }
    }
}