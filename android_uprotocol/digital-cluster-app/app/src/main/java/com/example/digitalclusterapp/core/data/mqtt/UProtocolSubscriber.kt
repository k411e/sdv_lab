package com.example.digitalclusterapp.core.data.mqtt

import android.util.Log
import com.example.digitalclusterapp.core.domain.model.ClusterState
import com.example.digitalclusterapp.core.domain.model.VehicleType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.eclipse.uprotocol.transport.UListener
import org.eclipse.uprotocol.transport.UTransport
import org.eclipse.uprotocol.uri.serializer.UriSerializer
import org.eclipse.uprotocol.v1.UMessage
import org.eclipse.uprotocol.v1.UUri
import org.json.JSONObject
import java.util.Calendar
import java.util.Optional

/**
 * Direct MQTT subscriber for raw MQTT topics (not uProtocol).
 * Specifically subscribes to the "vehicle/parameters" topic from ClusterDataInjector.
 */
class UProtocolSubscriber(
    private val transport : UTransport,
    private val scope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    companion object {
        private const val TAG = "DirectMqttSubscriber"
        private const val VEHICLE_PARAMETERS_TOPIC = "up://cruise-control.app/C110/1/8000"
        private val VEHICLE_PARAMETERS_TOPIC_UURI : UUri = UriSerializer.deserialize(VEHICLE_PARAMETERS_TOPIC)
    }

    private val _state = MutableStateFlow(ClusterState())
    val state: StateFlow<ClusterState> = _state.asStateFlow()

    /**
     * Starts subscribing to the vehicle/parameters topic
     */
    fun start() {
        scope.launch(coroutineDispatcher) {
            try {
                val listener = UListener { message ->
                    scope.launch(coroutineDispatcher) {
                        Log.i(TAG, "Successfully received : $VEHICLE_PARAMETERS_TOPIC")
                        handleMessage(message)
                    }
                }
                transport.registerListener( VEHICLE_PARAMETERS_TOPIC_UURI,
                    Optional.empty(), listener)
                    .whenComplete { _, throwable ->
                        if(throwable != null)
                        {
                            Log.e(TAG, "Failed to subscribe to topic: $VEHICLE_PARAMETERS_TOPIC ${throwable.message}")
                        }
                        else
                        {
                            Log.e(TAG, "Successfully subscribed to topic: $VEHICLE_PARAMETERS_TOPIC at ${Calendar.getInstance().time}")

                        }
                    }

            } catch (e: Exception) {
                Log.e(TAG, "Failed to subscribe to topic: $VEHICLE_PARAMETERS_TOPIC", e)
            }
        }
    }

    /**
     * Handles incoming MQTT messages from the vehicle/parameters topic
     */
    private fun handleMessage(message: UMessage) {
        val payload = message.payload
        val text = runCatching { payload.toString(Charsets.UTF_8) }.getOrNull() ?: return

        Log.d(TAG, "Received message from $VEHICLE_PARAMETERS_TOPIC: $text")

        // Try to parse as JSON (ClusterDataInjector format)
        val jsonData = runCatching { JSONObject(text) }.getOrNull()
        if (jsonData != null) {
            handleJsonMessage(jsonData)
            return
        }

        // Fallback to legacy key-value format
        val pairs = parsePairs(text)
        if (pairs.isNotEmpty()) {
            handleKeyValueMessage(pairs)
        }
    }

    /**
     * Handles JSON format messages from ClusterDataInjector
     */
    private fun handleJsonMessage(jsonData: JSONObject) {
        val current = _state.value
        var next = current

        try {
            // Map JSON fields to ClusterState properties
            jsonData.optInt("Speed", current.speed).let { if (it != current.speed) next = next.copy(speed = it) }
            jsonData.optBoolean("CruiseControl", current.cruiseControl).let { if (it != current.cruiseControl) next = next.copy(cruiseControl = it) }
            jsonData.optDouble("RPM", current.rpm.toDouble()).toFloat().let { if (it != current.rpm) next = next.copy(rpm = it) }
            jsonData.optInt("Engine Temperature", current.engineTemp.toInt()).toFloat().let { if (it != current.engineTemp) next = next.copy(engineTemp = it) }
            jsonData.optString("Gear", current.gear.toString()).firstOrNull()?.let { if (it != current.gear) next = next.copy(gear = it) }
            jsonData.optInt("AmbientTemperature", current.ambientTempC).let { if (it != current.ambientTempC) next = next.copy(ambientTempC = it) }
            jsonData.optString("Economy", current.economy).let { if (it != current.economy) next = next.copy(economy = it) }
            jsonData.optString("SpeedUnit", current.speedUnit).let { if (it != current.speedUnit) next = next.copy(speedUnit = it) }
            jsonData.optInt("Battery", current.battery).let { if (it != current.battery) next = next.copy(battery = it) }
            jsonData.optInt("Range", current.rangeRemaining).let { if (it != current.rangeRemaining) next = next.copy(rangeRemaining = it) }
            jsonData.optInt("TemperatureUnit", current.tempUnit).let { if (it != current.tempUnit) next = next.copy(tempUnit = it) }
            jsonData.optBoolean("ShareLocation", current.location).let { if (it != current.location) next = next.copy(location = it) }
            
            // Handle TypeOfVehicle (0 = Petrol/Combust, 1 = Electric)
            val vehicleTypeInt = jsonData.optInt("TypeOfVehicle", -1)
            if (vehicleTypeInt != -1) {
                val vehicleType = if (vehicleTypeInt == 1) VehicleType.ELECTRIC else VehicleType.COMBUST
                if (vehicleType != current.typeOfVehicle) next = next.copy(typeOfVehicle = vehicleType)
            }

            // Only update state if something changed
            if (next != current) {
                _state.value = next
                Log.d(TAG, "Updated state from JSON: $next")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing JSON message", e)
        }
    }

    /**
     * Handles legacy key-value format messages for backward compatibility
     */
    private fun handleKeyValueMessage(pairs: Map<String, String>) {
        val current = _state.value
        var next = current

        pairs.forEach { (key, rawValue) ->
            when (key.lowercase()) {
                "speed" -> rawValue.toIntOrNull()?.let { next = next.copy(speed = it) }
                "cruisecontrol" -> rawValue.toBoolean().let { next = next.copy(cruiseControl = it) }
                "rpm" -> rawValue.toFloatOrNull()?.let { next = next.copy(rpm = it) }
                "enginetemp" -> rawValue.toFloatOrNull()?.let { next = next.copy(engineTemp = it) }
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
            Log.d(TAG, "Updated state from key-value: $next")
        }
    }

    /**
     * Parses text into key-value pairs
     * Accepts formats like: speed-100, speed:100, speed = 100, speed 100
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
