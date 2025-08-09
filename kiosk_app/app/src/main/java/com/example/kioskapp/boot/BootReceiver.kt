package com.example.kioskapp.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kioskapp.core.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Listens for BOOT_COMPLETED broadcasts.  When the user has enabled
 * autostart in the settings the receiver will launch the main activity.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Check the autostart preference asynchronously
            val prefs = Prefs
            CoroutineScope(Dispatchers.Main).launch {
                prefs.autostartFlow(context).collect { autostart ->
                    if (autostart) {
                        val launchIntent = Intent(context, com.example.kioskapp.ui.MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(launchIntent)
                    }
                }
            }
        }
    }
}