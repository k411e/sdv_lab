package com.example.digitalclusterapp.core.data.mqtt

import android.util.Log
import com.example.digitalclusterapp.core.domain.model.ClusterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.eclipse.uprotocol.v1.UMessage
import org.eclipse.uprotocol.v1.UUri

/**
 * Connects messages from MQTT/uProtocol to the ClusterState.
 * Parses incoming messages in formats like "speed-100" and updates the state accordingly.
 *
 * @param scope CoroutineScope for launching asynchronous operations
 */
class MqttClusterBinder(
    private val scope: CoroutineScope
) {

    companion object {
        private const val TAG = "MqttClusterBinder"
    }

    private val _state = MutableStateFlow(ClusterState())

    /**
     * Observable state flow of the cluster display
     */
    val state: StateFlow<ClusterState> = _state.asStateFlow()

    // URI for subscribing to all messages
    private val any: UUri = UUri.newBuilder()
        .setAuthorityName("*")
        .setUeId(0xffff)
        .setUeVersionMajor(0xff)
        .setResourceId(0xffff)
        .build()

    /**
     * Starts listening for MQTT messages and updates the cluster state accordingly
     */
    fun start() {
        // Register listener for all messages
        UProtoMqtt.transport.registerListener(any, any) { msg ->
            scope.launch(Dispatchers.Default) {
                Log.d(TAG, "handleMessage was called")
                handleMessage(msg)
            }
        }
    }

    /**
     * Updates the cluster state with a new state
     *
     * @param newState The new state to set
     */
    fun updateState(newState: ClusterState) {
        _state.value = newState
    }

    /**
     * Handles incoming MQTT messages and updates state accordingly
     *
     * @param msg The received UMessage
     */
    private fun handleMessage(msg: UMessage) {
        val bytes = msg.payload.toByteArray()
        val text = runCatching { String(bytes, Charsets.UTF_8) }.getOrNull() ?: return

        // Accept formats such as: speed-100 | speed:100 | speed = 100 | "speed 100"
        // ex.: "speed-100,rpm-2200\ngear-D"
        val pairs = parsePairs(text)

        if (pairs.isEmpty()) {
            return
        }

        // Update ClusterState with new values
        val current = _state.value
        var next = current

        pairs.forEach { (key, rawValue) ->
            when (key.lowercase()) {
                "speed" -> rawValue.toIntOrNull()?.let { next = next.copy(speed = it) }
                "cruisecontrol" -> rawValue.toBoolean().let { next = next.copy(cruiseControl = it) }
                "rpm" -> rawValue.toIntOrNull()?.let { next = next.copy(rpm = it) }
                "mode", "modetop" -> next = next.copy(modeTop = rawValue)
                "modemid" -> next = next.copy(modeMid = rawValue)
                "modebottom" -> next = next.copy(modeBottom = rawValue)
                "gear" -> rawValue.firstOrNull()?.let { next = next.copy(gear = it) }
                "ambienttempc", "ambient", "temp", "tempc" ->
                    rawValue.toIntOrNull()?.let { next = next.copy(ambientTempC = it) }

                "economy" -> next = next.copy(economy = rawValue)
            }
        }

        // Only update state if something changed
        if (next != current) {
            _state.value = next
            Log.d(TAG, "Updated state: $next")
        }
    }

    /**
     * Parses text into key-value pairs
     * Accepts formats like: speed-100, speed:100, speed = 100, speed 100
     *
     * @param text The text to parse
     * @return Map of key-value pairs
     */
    private fun parsePairs(text: String): Map<String, String> {
        Log.d(TAG, "Parsing message: $text")

        val result = mutableMapOf<String, String>()
        val chunks = text.split('\n', ',', ';')
        val regex = Regex("""^\s*([A-Za-z_][\w]*)\s*[-:=\s]\s*([A-Za-z0-9\.\+\-]+)\s*$""")

        for (c in chunks) {
            val m = regex.find(c) ?: continue
            val key = m.groupValues[1]
            val value = m.groupValues[2]
            result[key] = value
        }

        Log.d(TAG, "Parsed result: $result")
        return result
    }
}