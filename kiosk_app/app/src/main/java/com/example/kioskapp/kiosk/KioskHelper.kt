package com.example.kioskapp.kiosk

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log

/**
 * Utility class to enable kiosk (lock task) mode.  When kiosk mode is
 * enabled, the user cannot exit the activity using the home or back
 * buttons.  On devices provisioned with a device owner this will
 * permanently pin the app.  Otherwise it falls back to screen pinning
 * requiring the user to long‑press the back and overview buttons to exit.
 */
object KioskHelper {
    fun enableKiosk(activity: Activity) {
        try {
            activity.startLockTask()
        } catch (e: IllegalStateException) {
            Log.w("KioskHelper", "Unable to start lock task: ${e.message}")
        }
    }

    fun disableKiosk(activity: Activity) {
        try {
            activity.stopLockTask()
        } catch (e: IllegalStateException) {
            Log.w("KioskHelper", "Unable to stop lock task: ${e.message}")
        }
    }
}