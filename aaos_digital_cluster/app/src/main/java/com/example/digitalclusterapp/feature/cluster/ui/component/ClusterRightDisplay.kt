package com.example.digitalclusterapp.feature.cluster.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.digitalclusterapp.core.domain.model.ClusterState
import com.example.digitalclusterapp.core.domain.model.VehicleType

@Composable
fun ClusterRightDisplay(
    modifier: Modifier = Modifier,
    state: ClusterState
) {

    when (state.typeOfVehicle) {
        VehicleType.COMBUST ->
            ClusterRpmDisplay(
                modifier,
                rpm = state.rpm
            )
        //TODO -> Create the electric
        VehicleType.ELECTRIC ->
            ClusterBatteryDisplay(
                modifier,
                rpm = state.rpm
            )
    }
}