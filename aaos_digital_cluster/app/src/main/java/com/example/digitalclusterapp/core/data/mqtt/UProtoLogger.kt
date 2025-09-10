package com.example.digitalclusterapp.core.data.mqtt

import android.util.Log
import org.eclipse.uprotocol.v1.UMessage
import org.eclipse.uprotocol.v1.UUri

/**
 * Utility for logging uProtocol messages.
 * Helps with debugging by printing message details to logcat.
 */
object UProtoLogger {

    private const val TAG = "uProto"

    /**
     * Starts logging all uProtocol messages
     */
    fun startLoggingAll() {
        Log.i(TAG, "Starting to log all uProtocol messages")

        // Create wildcard URIs to match any source and sink
        val anySource = UUri.newBuilder()
            .setAuthorityName("*")
            .setUeId(0xffff)
            .setUeVersionMajor(0xff)
            .setResourceId(0xffff)
            .build()

        val anySink = UUri.newBuilder()
            .setAuthorityName("*")
            .setUeId(0xffff)
            .setUeVersionMajor(0xff)
            .setResourceId(0xffff)
            .build()

        // Register listener for all messages
        UProtoMqtt.transport.registerListener(anySource, anySink) { msg ->
            dump(msg)
        }
    }

    /**
     * Logs the details of a UMessage
     *
     * @param msg The message to log
     */
    private fun dump(msg: UMessage) {
        val a = msg.attributes
        val payload = msg.payload.toByteArray()

        // Log message attributes
        Log.i(TAG, "---- UMessage ----")
        Log.i(TAG, "type=${a.type} ttl=${if (a.hasTtl()) a.ttl else "NA"}")
        if (a.hasId()) Log.i(TAG, "id=${a.id}")
        if (a.hasReqid()) Log.i(TAG, "reqid=${a.reqid}")
        if (a.hasSource()) Log.i(TAG, "source=${a.source}")
        if (a.hasSink()) Log.i(TAG, "sink=${a.sink}")
        if (a.hasPermissionLevel()) Log.i(TAG, "perm=${a.permissionLevel}")
        if (a.hasCommstatus()) Log.i(TAG, "commstatus=${a.commstatus}")
        if (a.hasToken()) Log.i(TAG, "token=${a.token}")
        if (a.hasTraceparent()) Log.i(TAG, "traceparent=${a.traceparent}")

        // Log payload in appropriate format
        Log.i(TAG, "payloadBytes=${payload.size}")

        // Try to interpret payload as UTF-8 text if it contains printable characters
        val asUtf8 = runCatching { String(payload, Charsets.UTF_8) }.getOrNull()
        if (asUtf8 != null && asUtf8.all { it.isDefined() && it.code in 32..126 || it == '\n' }) {
            Log.i(TAG, "payload(utf8)=\n$asUtf8")
        } else {
            // Otherwise log as hex bytes
            Log.i(TAG, "payload(hex)= ${payload.joinToString(" ") { "%02x".format(it) }}")
        }
    }
}