package com.example.digitalclusterapp.feature.cluster.ui.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.digitalclusterapp.core.domain.model.CentralScreenState

@Composable
fun ClusterMiddleDisplay(
    modifier: Modifier = Modifier,
    current: CentralScreenState = CentralScreenState.MODES,

    ) {

    when (current) {
        CentralScreenState.MODES -> ClusterModeDisplay(
            modifier = modifier,
            modeTop = "Race",
            modeMid = "Sport+",
            modeBottom = "City"
        )
        CentralScreenState.MAP -> ClusterMapDisplay(modifier)
        CentralScreenState.SENSORS -> ClusterSensorDisplay(modifier)
    }
}