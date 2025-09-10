package com.example.digitalclusterapp.core.data.repository

import com.example.digitalclusterapp.core.data.mqtt.MqttClusterBinder
import com.example.digitalclusterapp.core.data.mqtt.MqttPublisher
import com.example.digitalclusterapp.core.domain.model.ClusterState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MqttClusterRepositoryImpl @Inject constructor(
    private val binder: MqttClusterBinder,
    private val publisher: MqttPublisher
) : MqttClusterRepository {

    override val state: StateFlow<ClusterState> = binder.state

    override suspend fun updateSpeed(speed: Int): Boolean {
        return publisher.publishKeyValue("speed", speed)
    }

    override suspend fun updateRpm(rpm: Int): Boolean {
        return publisher.publishKeyValue("rpm", rpm)
    }

    override suspend fun toggleCruiseControl(): Boolean {
        val currentState = state.value
        val newValue = !currentState.cruiseControl
        return publisher.publishKeyValue("cruisecontrol", newValue)
    }

    override suspend fun updateClusterState(newState: ClusterState) {
        binder.updateState(newState)
    }

    override suspend fun publishKeyValue(key: String, value: Any): Boolean {
        return publisher.publishKeyValue(key, value)
    }
}