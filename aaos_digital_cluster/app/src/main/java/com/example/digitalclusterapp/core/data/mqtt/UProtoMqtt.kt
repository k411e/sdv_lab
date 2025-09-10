package com.example.digitalclusterapp.core.data.mqtt

import android.util.Log
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.eclipse.uprotocol.mqtt.TransportFactory
import org.eclipse.uprotocol.transport.UTransport
import org.eclipse.uprotocol.v1.UUri
import java.util.concurrent.TimeUnit

/**
 * Singleton object that manages MQTT connection and transport for uProtocol.
 */
object UProtoMqtt {

    private const val TAG = "UProtoMqtt"

    /**
     * Raw MQTT client instance
     */
    val raw = Mqtt5Client.builder()
        .identifier("android-" + System.currentTimeMillis())
        .serverHost("10.0.2.2")  // Default for Android emulator connecting to host
        .serverPort(1883)        // Standard MQTT port (use 8883 for TLS)
        // .sslWithDefaultConfig() // Uncomment for TLS
        .build()

    /**
     * Source URI for this device
     */
    private val mySource: UUri = UUri.newBuilder()
        .setAuthorityName("device")
        .setUeId(0x0001)
        .setUeVersionMajor(0x01)
        .setResourceId(0x1000)
        .build()

    /**
     * uProtocol transport instance using HiveMQ MQTT5 client
     */
    val transport: UTransport = TransportFactory.createInstance(mySource, raw)

    /**
     * Connects to the MQTT broker
     *
     * @param timeoutMs Connection timeout in milliseconds
     * @return true if connection was successful, false otherwise
     */
    suspend fun connect(timeoutMs: Long = 2_000): Boolean = withContext(Dispatchers.IO) {
        val fut = raw.toAsync().connectWith().send().orTimeout(timeoutMs, TimeUnit.MILLISECONDS)
        try {
            fut.await()
            Log.i(TAG, "Successfully connected to MQTT broker")
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
        transport.close()
    }
}