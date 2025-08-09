
package com.example.kioskapp.service

import android.app.*
import android.content.*
import android.os.*
import androidx.core.app.NotificationCompat
import com.example.kioskapp.R
import com.example.kioskapp.trigger.HttpTrigger
import com.example.kioskapp.trigger.BleGattTrigger
import com.example.kioskapp.trigger.TriggerSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class TriggerService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var http: HttpTrigger? = null
    private var ble: BleGattTrigger? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(1, buildNotification("Waiting for trigger…"))
        // NOTE: We only start listeners that are enabled in prefs (kept simple here)
        // You can wire this to Prefs to start/stop dynamically.
                    }
            }

    private fun buildNotification(text: String): Notification {
        val channelId = "kiosk_trigger"
        val mgr = getSystemService(NotificationManager::class.java)
        if (mgr.getNotificationChannel(channelId) == null) {
            mgr.createNotificationChannel(NotificationChannel(channelId, "Kiosk Trigger", NotificationManager.IMPORTANCE_MIN))
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Kiosk running")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setOngoing(true)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Lazy: start HTTP server always as an example
        if (http == null) {
                val port = Prefs.getInt(this, Prefs.HTTP_PORT, 8765)
                if (Prefs.getBool(this, Prefs.ENABLE_HTTP, true)) {
            http = HttpTrigger(this, port).also { it.start() }
        }
        // BLE left for manual start via settings if needed
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        http?.stop(); http = null
        ble?.stop(); ble = null
        scope.cancel()
        super.onDestroy()
    }
}
