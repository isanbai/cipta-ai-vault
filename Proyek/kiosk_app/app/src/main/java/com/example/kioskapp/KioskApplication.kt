package com.example.kioskapp

import android.app.Application
import android.util.Log

/**
 * Custom [Application] class used to initialise global services.  At the
 * moment it doesn’t perform any heavy initialisation but exists as a hook
 * should you need to set up libraries (e.g. logging or analytics).  The
 * application name is referenced in the manifest so the system will
 * instantiate this class on process start.
 */
class KioskApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // You could initialise logging here or set up a global coroutine scope.
        Log.d("KioskApplication", "Application created")
    }
}