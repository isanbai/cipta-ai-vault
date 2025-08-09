package com.example.kioskapp.trigger

import android.view.KeyEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Simple [TriggerSource] implementation based on key events.  The consumer
 * should forward key events from the host activity into [onKeyEvent] and the
 * trigger will update its internal state accordingly.  Two key codes are
 * configured: one to indicate the ON state and one for OFF.  Only ACTION_DOWN
 * events are acted upon.
 */
open class BaseKeyEventTrigger(
    private val keyOn: Int,
    private val keyOff: Int
) : TriggerSource {
    private val _events = MutableStateFlow(false)
    override val events: StateFlow<Boolean> = _events.asStateFlow()

    override fun start() {
        // No‑op for key based triggers
    }

    override fun stop() {
        // No‑op for key based triggers
    }

    /**
     * Forward a [KeyEvent] from your activity to this trigger.  Returns true
     * if the event should be consumed and not passed further down the chain.
     */
    open fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN) return false
        when (event.keyCode) {
            keyOn -> _events.value = true
            keyOff -> _events.value = false
            else -> return false
        }
        return true
    }
}