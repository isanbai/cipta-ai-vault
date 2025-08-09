package com.example.kioskapp.trigger

import android.view.KeyEvent

/**
 * Trigger that reacts to Bluetooth HID devices (e.g. selfie remotes).  This
 * implementation simply maps volume up and down keys to ON and OFF states.
 */
import androidx.media.session.MediaButtonReceiver
import androidx.media.session.MediaSessionCompat
class BtHidTrigger : BaseKeyEventTrigger(
    keyOn = KeyEvent.KEYCODE_VOLUME_UP,
    keyOff = KeyEvent.KEYCODE_VOLUME_DOWN
)