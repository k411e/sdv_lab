package com.example.digitalclusterapp.feature.cluster.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.digitalclusterapp.core.designsystem.theme.LocalClusterTypography

@Composable
fun ClusterSensorDisplay(
    modifier: Modifier = Modifier
) {
    val typography = LocalClusterTypography.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Im a Sensor Assistant",
            style = typography.subCenter
        )
    }
}