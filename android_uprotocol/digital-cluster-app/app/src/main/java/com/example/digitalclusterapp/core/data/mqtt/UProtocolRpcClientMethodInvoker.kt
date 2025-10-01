package com.example.digitalclusterapp.core.data.mqtt

import android.util.Log
import com.example.digitalclusterapp.core.domain.model.ClusterState
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.uprotocol.communication.CallOptions
import org.eclipse.uprotocol.communication.InMemoryRpcClient
import org.eclipse.uprotocol.communication.UPayload
import org.eclipse.uprotocol.transport.LocalUriProvider
import org.eclipse.uprotocol.transport.StaticUriProvider
import org.eclipse.uprotocol.transport.UTransport
import org.eclipse.uprotocol.uri.serializer.UriSerializer
import org.eclipse.uprotocol.v1.UPayloadFormat
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicLong

class UProtocolRpcClientMethodInvoker ( private val tranport : UTransport) {

        private val TAG = "MqttPublisher"

        private val messageCounter = AtomicLong(1)

        private val SET_TARGET_OPERATION_RPC = "up://cruise-control.app/C110/1/1"

        private val LOCAL_SERVICE_UURI = "up://android-cruise-control.app/BBB/1/0"

        private val TARGET_OPERATION_UURI = UriSerializer.deserialize(SET_TARGET_OPERATION_RPC)

        private var uirProvider : LocalUriProvider = StaticUriProvider.of(UriSerializer.deserialize(LOCAL_SERVICE_UURI))
        private  var client : InMemoryRpcClient = InMemoryRpcClient(tranport,uirProvider)

    suspend fun publishText(text: String): Boolean =
        withContext(Dispatchers.IO) {

            try {
                var byteBuffer = ByteBuffer.allocate(4)
                byteBuffer.putFloat(1.0f).flip()

                var payload : UPayload = UPayload.pack(
                    ByteString.copyFrom(byteBuffer),
                    UPayloadFormat.UPAYLOAD_FORMAT_RAW
                )

                //create a really big time to live, due to emulated devices
                var callOpt = CallOptions(60*60*24*1000)

                client.invokeMethod(TARGET_OPERATION_UURI,payload, callOpt).whenComplete {
                   completions, throwable ->
                   if (throwable != null)
                   {
                       Log.e(TAG, "There was a problem requesting the RPC method for ${TARGET_OPERATION_UURI} ${throwable}")
                   }
                    else{
                           Log.e(TAG, "RPC OK!!!!!!!!!!!!!!!!!!!! ${TARGET_OPERATION_UURI}")
                    }

               }
                return@withContext true
            } catch (e: Exception) {
                Log.e(TAG, "Error publishing message via uProtocol", e)
                return@withContext false
            }
        }

    suspend fun publishKeyValue(key: String, value: Any): Boolean {
        val formattedMessage = "$key-$value"
        return publishText(formattedMessage)
    }

    /**
     * Publishes ClusterState as JSON format compatible with ClusterDataInjector
     *
     * @param clusterState The cluster state to publish
     * @return true if successful, false otherwise
     */
    suspend fun publishClusterStateAsJson(clusterState: ClusterState): Boolean =

        withContext(Dispatchers.IO) {
            try {
            /*
            val json = JSONObject().apply {
            put(
                "TypeOfVehicle",
                if (clusterState.typeOfVehicle == VehicleType.ELECTRIC) 1 else 0
            )
            put("Gear", clusterState.gear.toString())
            put("CruiseControl", clusterState.cruiseControl)
            put("ShareLocation", clusterState.location)
            put("Speed", clusterState.speed)
            put("SpeedUnit", clusterState.speedUnit)
            put("RPM", clusterState.rpm)
            put("Economy", clusterState.economy)
            put("AmbientTemperature", clusterState.ambientTempC)
            put("EngineTemperature", clusterState.engineTemp.toInt())
            put("TemperatureUnit", clusterState.tempUnit)
            put("Battery", clusterState.battery)
            put("Range", clusterState.rangeRemaining)
            put("ModeTop", clusterState.modeTop)
            put("ModeMid", clusterState.modeMid)
            put("ModeBottom", clusterState.modeBottom)
            put("CentralScreen", clusterState.currentCentralScreen.ordinal)
        }

            val jsonString = json.toString()
            Log.d(TAG, "Publishing ClusterState as JSON: $jsonString")

            */
                return@withContext publishText("")
            } catch (e: Exception) {
                Log.e(TAG, "Error creating JSON from ClusterState", e)
                return@withContext false
            }
        }
}