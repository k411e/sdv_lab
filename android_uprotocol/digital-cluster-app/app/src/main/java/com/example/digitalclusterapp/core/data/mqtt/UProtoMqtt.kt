package com.example.digitalclusterapp.core.data.mqtt

import android.util.Log
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.eclipse.uprotocol.mqtt.HiveMqTransportFactory
import org.eclipse.uprotocol.mqtt.TransportMode
import org.eclipse.uprotocol.transport.UTransport
import org.eclipse.uprotocol.v1.UUri
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Singleton object that manages MQTT connection and transport for uProtocol.
 */
class UProtoMqtt () {

    private val TAG = "UProtoMqtt"

    /**
     * Raw MQTT client instance
     */
    lateinit var mqttClient : Mqtt5BlockingClient

    lateinit var subscriber : UProtocolSubscriber

    lateinit var publisher : UProtocolRpcClientMethodInvoker

    /**
     * uProtocol transport instance using HiveMQ MQTT5 client
     */
    lateinit var transport: UTransport

    /**
     * Connects to the MQTT broker
     *
     * @param timeoutMs Connection timeout in milliseconds
     * @return true if connection was successful, false otherwise
     */
    suspend fun connect(timeoutMs: Long = 5_000): Boolean = withContext(Dispatchers.IO) {
        val BROKER_HOST = "10.0.2.2" // Replace with your broker host
        val BROKER_PORT = 1883 // Default MQTT port
        val CLIENT_ID_PREFIX = "AndroidCluster-"
        val clientId = CLIENT_ID_PREFIX + UUID.randomUUID().toString()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        mqttClient =Mqtt5Client.builder()
            .identifier(clientId)
            .serverHost(BROKER_HOST)
            .serverPort(BROKER_PORT)
            .automaticReconnect()
            .initialDelay(1, TimeUnit.SECONDS)
            .maxDelay(120, TimeUnit.SECONDS)
            .applyAutomaticReconnect()
            .buildBlocking()
        // MQTT Configuration
        mqttClient.connectWith()
            .cleanStart(true)
            .send()

        try {

            transport = HiveMqTransportFactory.createInstance(
                mqttClient,
                TransportMode.IN_VEHICLE,
                "device"
            )

            subscriber = UProtocolSubscriber(transport, scope)
            publisher = UProtocolRpcClientMethodInvoker(transport)

            Log.i(TAG, "Uprotocol connection initialized")
            true
        } catch (ce: CancellationException) {
            Log.w(TAG, "Connection attempt was cancelled")
            throw ce
        } catch (t: Throwable) {
            // Handle connection errors (UnknownHostException, ConnectException, SSLException, etc.)
            Log.w(TAG, "Connection failed: ${t.javaClass.simpleName}: ${t.message}")
            false
        }
    }

    /**
     * Closes the MQTT connection
     */
    fun close() {
        Log.i(TAG, "Closing MQTT connection")
        if (::mqttClient.isInitialized )
            mqttClient.disconnect()
    }
}