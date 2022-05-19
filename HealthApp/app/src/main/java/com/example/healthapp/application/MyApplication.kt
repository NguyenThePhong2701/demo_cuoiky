package com.example.healthapp.application

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyApplication : Application() {
    companion object {
        @JvmField val CHANNEL_ID = "channel_service_example"
    }

    override fun onCreate() {
        super.onCreate()
        createChannelNotification()
    }

    @SuppressLint("ObsoleteSdkInt", "WrongConstant")
    private fun createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel Service Example",
                NotificationManager.IMPORTANCE_MAX
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}