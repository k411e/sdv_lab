package com.example.digitalclusterapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.digitalclusterapp.core.data.mqtt.UProtoLogger
import com.example.digitalclusterapp.core.data.mqtt.UProtoMqtt
import com.example.digitalclusterapp.core.designsystem.theme.DigitalClusterAppTheme
import com.example.digitalclusterapp.feature.cluster.ui.screen.ClusterScreen
import com.example.digitalclusterapp.feature.cluster.viewmodel.ClusterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Main activity for the Digital Cluster application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val clusterViewModel: ClusterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigitalClusterAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ClusterScreen(viewModel = clusterViewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            UProtoMqtt.connect()           // connect to broker
            UProtoLogger.startLoggingAll() // subscribe and print
        }
    }

    override fun onStop() {
        super.onStop()
        UProtoMqtt.close()
    }
}