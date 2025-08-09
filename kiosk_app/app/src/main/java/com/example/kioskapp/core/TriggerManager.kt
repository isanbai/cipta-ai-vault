package com.example.kioskapp.core

import android.content.Context
import android.view.KeyEvent
import com.example.kioskapp.core.Prefs.TriggerType
import com.example.kioskapp.trigger.BleGattTrigger
import com.example.kioskapp.trigger.BtHidTrigger
import com.example.kioskapp.trigger.HeadsetTrigger
import com.example.kioskapp.trigger.HttpTrigger
import com.example.kioskapp.trigger.TriggerSource
import com.example.kioskapp.trigger.UsbHidTrigger
import com.example.kioskapp.trigger.VolumeTrigger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Manages the currently selected [TriggerSource] according to user preferences.
 * A single [events] flow exposes the trigger state regardless of which
 * implementation is active.  When the trigger type preference changes the
 * previous trigger is stopped and a new one is started.  Consumers must
 * call [start] and [stop] to hook into the lifecycle of the host activity.
 */
class TriggerManager(
    private val context: Context,
    private val scope: CoroutineScope,
    private val prefs: Prefs
) {
    private var triggerSource: TriggerSource? = null
    private var monitorJob: Job? = null
    private val _events = MutableStateFlow(false)
    val events: StateFlow<Boolean> = _events

    /** Starts monitoring the DataStore for trigger type changes and the active
     * trigger’s events. */
    fun start() {
        // Monitor preference changes and switch triggers when needed
        monitorJob = scope.launch {
            prefs.triggerTypeFlow(context).collect { typeName ->
                val type = TriggerType.valueOf(typeName)
                switchTrigger(type)
            }
        }
    }

    /** Stop monitoring and release the current trigger. */
    fun stop() {
        monitorJob?.cancel()
        triggerSource?.stop()
        triggerSource = null
    }

    /** Forwards a [KeyEvent] to the current trigger if it supports key based
     * events.  Returns true if the event should be consumed. */
    fun onKeyEvent(event: KeyEvent): Boolean {
        return when (val current = triggerSource) {
            is com.example.kioskapp.trigger.BaseKeyEventTrigger -> current.onKeyEvent(event)
            else -> false
        }
    }

    private fun switchTrigger(type: TriggerType) {
        // Stop and dispose the old trigger
        triggerSource?.stop()
        triggerSource = null
        // Construct new trigger based on the selected type
        val newTrigger: TriggerSource = when (type) {
            TriggerType.USB_HID -> UsbHidTrigger()
            TriggerType.BT_HID -> BtHidTrigger()
            TriggerType.HEADSET -> HeadsetTrigger(context)
            TriggerType.HTTP -> HttpTrigger()
            TriggerType.BLE_GATT -> {
                // Use the user‑defined service/characteristic UUIDs if available.  These could be stored in Prefs.
                // For demonstration we use the values from the specification placeholders.
                val serviceUuid = UUID.fromString("00001234-0000-1000-8000-00805f9b34fb")
                val charUuid = UUID.fromString("00005678-0000-1000-8000-00805f9b34fb")
                BleGattTrigger(context, serviceUuid, charUuid)
            }
            TriggerType.VOLUME -> VolumeTrigger()
        }
        triggerSource = newTrigger
        newTrigger.start()
        // Collect events from the new trigger
        scope.launch {
            newTrigger.events.collect { state -> _events.value = state }
        }
    }
}