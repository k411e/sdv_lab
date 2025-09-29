package com.example.digitalclusterapp.core.data.repository

import com.example.digitalclusterapp.core.data.mqtt.UProtoMqtt
import com.example.digitalclusterapp.core.data.mqtt.UProtocolSubscriber
import com.example.digitalclusterapp.core.data.mqtt.UProtocolRpcClientMethodInvoker
import com.example.digitalclusterapp.core.domain.model.ClusterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MqttClusterRepositoryImpl @Inject constructor(
    private val uprotoMqtt: UProtoMqtt,
) : MqttClusterRepository {

    // Combine both uProtocol and direct MQTT state flows
    private val _combinedState = MutableStateFlow(ClusterState())
    override val state: StateFlow<ClusterState> = _combinedState.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)


    override suspend fun startConnection()
    {
        uprotoMqtt.connect()
        uprotoMqtt.subscriber.start()
        // Collect from both flows and update the combined state
        scope.launch {
            uprotoMqtt.subscriber.state.collect {
                _combinedState.value = it
            }
        }
    }

    override fun stopConnection()
    {
        uprotoMqtt.close()
    }

    override suspend fun updateSpeed(speed: Int): Boolean {
        // Get current state and update only the speed
        val currentState = state.value
        val updatedState = currentState.copy(speed = speed)
        return uprotoMqtt.publisher.publishClusterStateAsJson(updatedState)
    }

    override suspend fun updateRpm(rpm: Int): Boolean {
        val currentState = state.value
        val updatedState = currentState.copy(rpm = rpm.toFloat())
        return uprotoMqtt.publisher.publishClusterStateAsJson(updatedState)
    }

    override suspend fun toggleCruiseControl(): Boolean {
        val currentState = state.value
        val updatedState = currentState.copy(cruiseControl = !currentState.cruiseControl)
        return uprotoMqtt.publisher.publishClusterStateAsJson(updatedState)
    }

    override suspend fun toggleLocation(): Boolean {
        val currentState = state.value
        val updatedState = currentState.copy(location = !currentState.location)
        return uprotoMqtt.publisher.publishClusterStateAsJson(updatedState)
    }

    override suspend fun updateClusterState(newState: ClusterState): Boolean {
        // Update the local state through the binder
        _combinedState.value = newState
        // Also publish the complete state to MQTT
        return uprotoMqtt.publisher.publishClusterStateAsJson(newState)
    }

    override suspend fun publishKeyValue(key: String, value: Any): Boolean {
        // For backward compatibility, keep this method but consider deprecating it
        return uprotoMqtt.publisher.publishKeyValue(key, value)
    }

}