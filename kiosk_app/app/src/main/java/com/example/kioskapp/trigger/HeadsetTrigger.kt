package com.example.kioskapp.trigger

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.KeyEvent
import androidx.media.session.MediaButtonReceiver
import androidx.media.session.MediaSessionCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Trigger that listens for headset media button events using
 * [MediaSessionCompat].  Play/pause or play maps to ON, and pause maps to OFF.
 */
class HeadsetTrigger(private val context: Context) : TriggerSource {
    private val _events = MutableStateFlow(false)
    override val events: StateFlow<Boolean> get() = _events.asStateFlow()

    private var mediaSession: MediaSessionCompat? = null

    override fun start() {
        if (mediaSession != null) return
        val session = MediaSessionCompat(context, "HeadsetTrigger")
        session.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        session.setCallback(object : MediaSessionCompat.Callback() {
            override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                val keyEvent = mediaButtonEvent?.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
                if (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (keyEvent.keyCode) {
                        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
                        KeyEvent.KEYCODE_MEDIA_PLAY -> _events.value = true
                        KeyEvent.KEYCODE_MEDIA_PAUSE -> _events.value = false
                    }
                }
                return true
            }
        })
        session.isActive = true
        mediaSession = session
    }

    override fun stop() {
        mediaSession?.run {
            isActive = false
            release()
        }
        mediaSession = null
    }
}