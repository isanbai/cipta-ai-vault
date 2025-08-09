package com.example.kioskapp.trigger

import android.view.KeyEvent

/**
 * Fallback trigger that interprets long‑presses on the device volume keys.  A
 * long press on volume up will set the trigger to ON while a long press on
 * volume down sets it to OFF.  Short presses are ignored so as not to
 * interfere with normal volume adjustment.  This trigger is useful for
 * development and demonstration purposes when no external hardware is
 * available.
 */
class VolumeTrigger : BaseKeyEventTrigger(
    keyOn = KeyEvent.KEYCODE_VOLUME_UP,
    keyOff = KeyEvent.KEYCODE_VOLUME_DOWN
) {
    override fun onKeyEvent(event: KeyEvent): Boolean {
        // Only act on long presses; ignore normal presses
        if (event.action == KeyEvent.ACTION_DOWN && event.isLongPress) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    // Use the parent implementation to update the state
                    return super.onKeyEvent(event)
                }
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    return super.onKeyEvent(event)
                }
            }
        }
        return false
    }
}