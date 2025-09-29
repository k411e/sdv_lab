package com.example.digitalclusterapp.core.data.di

import com.example.digitalclusterapp.core.data.mqtt.UProtoMqtt
import com.example.digitalclusterapp.core.data.mqtt.UProtocolSubscriber
import com.example.digitalclusterapp.core.data.mqtt.UProtocolRpcClientMethodInvoker
import com.example.digitalclusterapp.core.data.repository.MqttClusterRepository
import com.example.digitalclusterapp.core.data.repository.MqttClusterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MqttModule {


    @Provides
    @Singleton
    fun provideUprotoMqtt(): UProtoMqtt {
        return UProtoMqtt()
    }

    @Provides
    @Singleton
    fun provideClusterRepository(): MqttClusterRepository {
        return MqttClusterRepositoryImpl( provideUprotoMqtt())
    }
}