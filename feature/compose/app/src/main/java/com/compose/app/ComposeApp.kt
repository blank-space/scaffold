package com.compose.app

import android.app.Application
import com.example.compose.ComposableServiceManager

class ComposeApp:Application() {
    override fun onCreate() {
        super.onCreate()
        ComposableServiceManager.collectServices()
    }
}