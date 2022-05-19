package com.example.healthapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.healthapp.R
import com.example.healthapp.activity.AlarmOnActivity
import com.example.healthapp.application.MyApplication.Companion.CHANNEL_ID


class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.e("msg", "yes")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.alarm_song)
//        mp.start()
        val key = intent?.getStringExtra("key")
//        var key1 = key.toString()
        sendNotification(key)
        Log.e("key1", key.toString())
        return START_NOT_STICKY
    }

    private fun sendNotification(key: String?) {
        val i = Intent(this, AlarmOnActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)


        val notificationCompat = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Content title")
            .setSmallIcon(R.drawable.icon_fire)
            .setContentText(key.toString())
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notificationCompat)
//        stopSelf()
    }
}