package com.example.digitalclusterapp.core.data.mqtt

import android.util.Log
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.eclipse.uprotocol.v1.UAttributes
import org.eclipse.uprotocol.v1.UMessage
import org.eclipse.uprotocol.v1.UMessageType
import org.eclipse.uprotocol.v1.UUri

class MqttPublisher {
    companion object {
        private const val TAG = "MqttPublisher"

        // Default sink URI for cluster-related messages
        private val CLUSTER_SINK = UUri.newBuilder()
            .setAuthorityName("cluster")
            .setUeId(0x0001)
            .setUeVersionMajor(0x01)
            .setResourceId(0x2000)
            .build()
    }

    suspend fun publishText(text: String, sink: UUri = CLUSTER_SINK): Boolean =
        withContext(Dispatchers.IO) {
            try {
                // Create message attributes
                val attributes = UAttributes.newBuilder()
                    .setSource(UProtoMqtt.transport.source)
                    .setSink(sink)
                    .setType(UMessageType.UMESSAGE_TYPE_PUBLISH)
                    .build()

                // Create the message with text payload
                val message = UMessage.newBuilder()
                    .setAttributes(attributes)
                    .setPayload(ByteString.copyFrom(text.toByteArray(Charsets.UTF_8)))
                    .build()

                // Send the message
                val result = UProtoMqtt.transport.send(message).await()
                val success = result.code.number == 0 // OK status code is 0

                if (success) {
                    Log.d(TAG, "Successfully published message: $text")
                } else {
                    Log.w(TAG, "Failed to publish message: $text, status: ${result.code}")
                }

                return@withContext success
            } catch (e: Exception) {
                Log.e(TAG, "Error publishing message", e)
                return@withContext false
            }
        }

    suspend fun publishKeyValue(key: String, value: Any, sink: UUri = CLUSTER_SINK): Boolean {
        val formattedMessage = "$key-$value"
        return publishText(formattedMessage, sink)
    }
}