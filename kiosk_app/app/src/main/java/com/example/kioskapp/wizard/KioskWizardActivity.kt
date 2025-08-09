
package com.example.kioskapp.wizard

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kioskapp.R
import com.example.kioskapp.service.TriggerService
import com.example.kioskapp.util.BatteryUtils
import com.example.kioskapp.core.Prefs
import kotlinx.coroutines.*

class KioskWizardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kiosk_wizard)

        findViewById<Button>(R.id.btn_battery).setOnClickListener {
            BatteryUtils.requestIgnoreBatteryOptimizations(this)
        }

        findViewById<Button>(R.id.btn_autostart).setOnClickListener {
            // Many OEMs have proprietary autostart pages; we can't deep-link reliably.
            // Open app details so user can enable autostart/allow background.
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_notifications).setOnClickListener {
            if (Build.VERSION.SDK_INT >= 26) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.btn_start_service).setOnClickListener {
            val intent = Intent(this, TriggerService::class.java)
            if (Build.VERSION.SDK_INT >= 26) startForegroundService(intent) else startService(intent)
        }

        findViewById<Button>(R.id.btn_screen_pinning).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Screen Pinning")
                .setMessage("Aktifkan screen pinning: Settings → Security → Screen pinning. Setelah aktif, buka app ini dan tekan Overview lalu ikon pin. Untuk kiosk penuh, gunakan Device Owner.")
                .setPositiveButton("Buka Settings") { _, _ ->
                    startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                }
                .setNegativeButton("Tutup", null)
                .show()
        }

        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Reset Wizard")
                .setMessage("Ini akan mengembalikan beberapa preferensi (loop/mute/autostart/kiosk/debounce/trigger type) ke nilai default. Lanjutkan?")
                .setPositiveButton("Reset") { _, _ -> resetPrefs() }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun resetPrefs() {
        val prefs = Prefs()
        // Default values
        GlobalScope.launch(Dispatchers.IO) {
            prefs.setValue(this@KioskWizardActivity, Prefs.LOOP_VIDEO, true)
            prefs.setValue(this@KioskWizardActivity, Prefs.MUTE, true)
            prefs.setValue(this@KioskWizardActivity, Prefs.AUTOSTART, true)
            prefs.setValue(this@KioskWizardActivity, Prefs.KIOSK, false)
            prefs.setValue(this@KioskWizardActivity, Prefs.DEBOUNCE_MS, 200)
            prefs.setValue(this@KioskWizardActivity, Prefs.TRIGGER_TYPE, "USB_HID")
        }
    }
}
