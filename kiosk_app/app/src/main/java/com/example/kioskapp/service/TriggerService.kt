package com.example.kioskapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.kioskapp.R

class TriggerService: Service() {
    override fun onCreate() {
        super.onCreate()
        val chanId = "kiosk_trigger"
        val nm = getSystemService(NotificationManager::class.java)
        if (nm.getNotificationChannel(chanId) == null) {
            nm.createNotificationChannel(NotificationChannel(chanId, "Kiosk", NotificationManager.IMPORTANCE_MIN))
        }
        val n: Notification = NotificationCompat.Builder(this, chanId)
            .setContentTitle("Kiosk running")
            .setContentText("Waiting for trigger…")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        startForeground(1, n)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
}
