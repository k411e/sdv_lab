package com.example.digitalclusterapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main application class for the Digital Cluster app.
 */
@HiltAndroidApp
class DigitalClusterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any application-wide dependencies here
    }
}