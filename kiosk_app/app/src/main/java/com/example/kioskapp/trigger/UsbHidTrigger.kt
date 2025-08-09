package com.example.kioskapp.trigger

import android.view.KeyEvent

/**
 * Trigger that listens to USB HID keyboard events.  By default this maps
 * `KEYCODE_VOLUME_UP` to the ON state and `KEYCODE_VOLUME_DOWN` to OFF.  A
 * host activity should call [onKeyEvent] from its `dispatchKeyEvent` method.
 */
class UsbHidTrigger : BaseKeyEventTrigger(
    keyOn = KeyEvent.KEYCODE_VOLUME_UP,
    keyOff = KeyEvent.KEYCODE_VOLUME_DOWN
)