package com.example.digitalclusterapp.feature.cluster.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.digitalclusterapp.R
import com.example.digitalclusterapp.core.designsystem.theme.ClusterColors
import com.example.digitalclusterapp.core.domain.model.ClusterState
import com.example.digitalclusterapp.core.domain.action.ClusterAction

@Composable
fun ControlButtons(
    modifier: Modifier = Modifier,
    onAction: (ClusterAction) -> Unit,
    state: ClusterState
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(ClusterColors.BlueBlack)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Speed controls
        IconButton(onClick = { onAction(ClusterAction.Speed.Increase) }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(
                    id = if (state.cruiseControl)
                        R.drawable.ic_plus_active
                    else
                        R.drawable.ic_plus
                ),
                contentDescription = "Increase Speed",
                tint = Color.White
            )
        }

        // Decrease speed
        IconButton(onClick = { onAction(ClusterAction.Speed.Decrease) }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(
                    id = if (state.cruiseControl)
                        R.drawable.ic_minus_active
                    else
                        R.drawable.ic_minus
                ),
                contentDescription = "Decrease Speed",
                tint = Color.White
            )
        }

        // Activate Cruise Control
        IconButton(onClick = { onAction(ClusterAction.ToggleCruiseControl) }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(
                    id = if (state.cruiseControl)
                        R.drawable.ic_cruise_control_active
                    else
                        R.drawable.ic_cruise_control_default
                ),
                contentDescription = "Toggle (Not Active)",
                tint = Color.Gray // Gray to indicate it's not active
            )
        }

        // Front Parking Sensor
        IconButton(onClick = { onAction(ClusterAction.ParkingSensor.Front) }) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = R.drawable.ic_front_parking_sensor),
                contentDescription = "Front Parking Sensor",
                tint = Color.Red
            )
        }

        // Rear Parking Sensor
        IconButton(onClick = { onAction(ClusterAction.ParkingSensor.Rear) }) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = R.drawable.ic_rear_parking_sensor),
                contentDescription = "Rear Parking Sensor",
                tint = Color.Red
            )
        }

        // Location
        IconButton(onClick = { onAction(ClusterAction.ToggleLocation) }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(
                    id = if (state.location)
                        R.drawable.ic_location_active
                    else
                        R.drawable.ic_location_default
                ),
                contentDescription = "Location (Not Active)",
                tint = Color.Gray // Gray to indicate it's not active
            )
        }

        // Right Parking Sensor
        IconButton(onClick = { onAction(ClusterAction.ParkingSensor.Right) }) {
            Icon(
                modifier = Modifier.size(46.dp),
                painter = painterResource(id = R.drawable.ic_right_parking_sensor),
                contentDescription = "Right Parking Sensor",
                tint = Color.Red
            )
        }

        // Left Parking Sensor
        IconButton(onClick = { onAction(ClusterAction.ParkingSensor.Left) }) {
            Icon(
                modifier = Modifier.size(46.dp),
                painter = painterResource(id = R.drawable.ic_left_parking_sensor),
                contentDescription = "Left Parking Sensor",
                tint = Color.Red
            )
        }
    }
}