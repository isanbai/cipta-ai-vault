package com.example.kioskapp.core

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences

/**
 * Centralised definition of all preference keys used by the application.  The
 * keys are defined once here to avoid typos when reading or writing to the
 * DataStore.  According to the official Jetpack documentation, DataStore
 * persists key–value pairs asynchronously using Kotlin coroutines and
 * flows【83036416110107†L172-L176】.
 */
object Prefs {
    // Extension property to obtain the DataStore instance from a Context.  The
    // DataStore name acts as the file name on disk.
    private const val PREF_NAME = "kiosk_prefs"
    private val Context.dataStore by preferencesDataStore(name = PREF_NAME)

    // Keys for storing various settings
    val VIDEO_URI = stringPreferencesKey("video_uri")
    val IMAGE_URI = stringPreferencesKey("image_uri")
    val LOOP_VIDEO = booleanPreferencesKey("loop_video")
    val TRIGGER_TYPE = stringPreferencesKey("trigger_type")
    val DEBOUNCE_MS = intPreferencesKey("debounce_ms")
    val AUTOSTART = booleanPreferencesKey("autostart")
    val KIOSK = booleanPreferencesKey("kiosk")
    val MUTE = booleanPreferencesKey("mute")

    /**
     * Helper to read a [String] preference from the DataStore.  Returns a
     * [Flow] that emits updates whenever the value changes.
     */
    fun videoUriFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { it[VIDEO_URI] }

    fun imageUriFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { it[IMAGE_URI] }

    fun loopVideoFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[LOOP_VIDEO] ?: true }

    fun triggerTypeFlow(context: Context): Flow<String> =
        context.dataStore.data.map { prefs -> prefs[TRIGGER_TYPE] ?: TriggerType.VOLUME.name }

    fun debounceMsFlow(context: Context): Flow<Int> =
        context.dataStore.data.map { prefs -> prefs[DEBOUNCE_MS] ?: 200 }

    fun autostartFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[AUTOSTART] ?: false }

    fun kioskFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KIOSK] ?: false }

    fun muteFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[MUTE] ?: true }

    /**
     * Generic function to update a preference.  It uses suspend to ensure
     * sequential writes.  Consumers should call this from a coroutine
     * context (e.g. using lifecycleScope in an Activity).
     */
    suspend fun <T> setValue(context: Context, key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefs -> prefs[key] = value }
    }

    /**
     * Enum representing all available trigger sources.  The names match
     * DataStore values so they should not be localised.
     */
    enum class TriggerType {
        USB_HID,
        BT_HID,
        HEADSET,
        HTTP,
        BLE_GATT,
        VOLUME
    }
}

// Keys for storing various settings
val VIDEO_URI = stringPreferencesKey("video_uri")
val IMAGE_URI = stringPreferencesKey("image_uri")
val LOOP_VIDEO = booleanPreferencesKey("loop_video")
val MUTE = booleanPreferencesKey("mute")
val AUTOSTART = booleanPreferencesKey("autostart")
val KIOSK = booleanPreferencesKey("kiosk")
val DEBOUNCE_MS = intPreferencesKey("debounce_ms")
val TRIGGER_TYPE = stringPreferencesKey("trigger_type")

// Service toggles
val ENABLE_HTTP = booleanPreferencesKey("enable_http")
val ENABLE_BLE = booleanPreferencesKey("enable_ble")

// HTTP settings
val HTTP_PORT = intPreferencesKey("http_port")

// BLE settings
val BLE_DEVICE_NAME = stringPreferencesKey("ble_device_name")
val BLE_SERVICE_UUID = stringPreferencesKey("ble_service_uuid")
val BLE_CHAR_UUID = stringPreferencesKey("ble_char_uuid")
