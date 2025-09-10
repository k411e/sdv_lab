package com.example.digitalclusterapp.feature.cluster.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.digitalclusterapp.R
import com.example.digitalclusterapp.core.domain.action.ClusterAction
import com.example.digitalclusterapp.core.domain.model.ClusterState
import com.example.digitalclusterapp.feature.cluster.ui.component.ClusterMiddleDisplay
import com.example.digitalclusterapp.feature.cluster.ui.component.ClusterRightDisplay
import com.example.digitalclusterapp.feature.cluster.ui.component.ClusterSpeedDisplay
import com.example.digitalclusterapp.feature.cluster.ui.component.ClusterTopBar
import com.example.digitalclusterapp.feature.cluster.ui.component.ControlButtons

/**
 * A modern automotive instrument cluster UI component.
 *
 * @param state The current state of the cluster display
 * @param modifier Modifier to be applied to the component
 * @param aspectRatio The width to height ratio of the component
 * @param cornerRadius The corner radius of the cluster display
 */
@Composable
fun Cluster(
    modifier: Modifier = Modifier,
    state: ClusterState,
    onAction: (ClusterAction) -> Unit,
    aspectRatio: Float = 964f / 373f,
    cornerRadius: Dp = 180.dp
) {
    Box(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(cornerRadius))
            .padding(20.dp)
    ) {
        // Background
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        // Central mode display with top icons
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 30.dp),
            // Todo uncomment when cuttlefish be ready
            //.padding(600.dp, 37.dp, 400.dp, 630.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClusterTopBar(
                modifier = Modifier.width(1100.dp),  // Width to accommodate icons
                cruiseControl = state.cruiseControl
            )
        }

        // Central mode display
//        ClusterModeDisplay(
//            modifier = Modifier
//                .align(Alignment.Center)
//                .padding(top = 6.dp),
//            modeTop = state.modeTop,
//            modeMid = state.modeMid,
//            modeBottom = state.modeBottom
//        )

        ClusterMiddleDisplay(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 6.dp),
            current = state.currentCentralScreen
        )

        // Speed display
        ClusterSpeedDisplay(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset(x = 12.dp.roundToPx(), y = 0) },
            speed = state.speed,
            speedUnit = state.speedUnit
        )

        // Right Side Display
        ClusterRightDisplay(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            state = state
        )

        // Bottom status bar
        // ClusterBottomStatusBar(
        //     modifier = Modifier
        //         .align(Alignment.BottomCenter)
        //         .width(900.dp)
        //         .padding(bottom = 17.dp),
        //     economy = state.economy,
        //     temperature = "${state.ambientTempC}°C"
        // )

        // Gear indicator
        // ClusterGearIndicator(
        //     modifier = Modifier
        //         .align(Alignment.BottomEnd)
        //         .padding(bottom = 306.dp, end = 589.dp),
        //     currentGear = state.gear
        // )
    }
    Box(
        Modifier.padding(top = 90.dp)
    ) {
        ControlButtons(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 20.dp)
                .width(500.dp),
            onAction = onAction,
            state = state
        )
    }
}

@Preview(widthDp = 2000, heightDp = 818)
@Composable
fun ClusterPreview() {
    Cluster(
        modifier = Modifier.fillMaxSize(),
        state = ClusterState(),
        onAction = { /* Preview Only */ }
    )
}