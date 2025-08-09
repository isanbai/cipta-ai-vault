package com.example.kioskapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kioskapp.core.Prefs
import com.example.kioskapp.core.StateMachine
import com.example.kioskapp.core.TriggerManager
import com.example.kioskapp.databinding.ActivityMainBinding
import com.example.kioskapp.image.ImageController
import com.example.kioskapp.kiosk.KioskHelper
import com.example.kioskapp.player.VideoController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

/**
 * The main activity drives the user interface for kiosk mode.  It displays
 * either a looping video or a static image/GIF based on the current
 * trigger state.  The activity stays in immersive full‑screen mode and
 * forwards key events to the [TriggerManager].
 */
class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var videoController: VideoController
    private lateinit var imageController: ImageController
    private lateinit var triggerManager: TriggerManager
    private lateinit var stateMachine: StateMachine
    private var stateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Hide the system bars for an immersive experience
        enterImmersiveMode()
        // Keep the screen on while in this activity
        binding.root.keepScreenOn = true
        // Initialise controllers
        videoController = VideoController(this, binding.playerView)
        imageController = ImageController(this, binding.imageView)
        // Prefs helper
        val prefs = Prefs
        // Create trigger manager and state machine
        triggerManager = TriggerManager(this, lifecycleScope, prefs)
        stateMachine = StateMachine(lifecycleScope, triggerManager.events, prefs.debounceMsFlow(this))
        // Observe preferences and state
        observeState(prefs)
        // Settings button launches configuration UI
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, com.example.kioskapp.settings.SettingsActivity::class.java))
        }
        // Pin the activity if kiosk mode is enabled
        lifecycleScope.launch {
            prefs.kioskFlow(this@MainActivity).collect { enabled ->
                if (enabled) {
                    KioskHelper.enableKiosk(this@MainActivity)
                }
            }
        }
    }

    private fun observeState(prefs: Prefs) {
        // Collect state machine state and update UI accordingly
        stateJob?.cancel()
        stateJob = lifecycleScope.launch {
            // Combine flows of state and preferences for URIs and loop
            stateMachine.state.collect { state ->
                when (state) {
                    StateMachine.State.ACTIVE -> {
                        // Get latest video URI and loop/mute preferences and play
                        val uriString = prefs.videoUriFlow(this@MainActivity).first()
                        if (uriString != null) {
                            val loop = prefs.loopVideoFlow(this@MainActivity).first()
                            val mute = prefs.muteFlow(this@MainActivity).first()
                            val uri = Uri.parse(uriString)
                            videoController.prepare(uri, loop, mute)
                            videoController.play()
                            binding.playerView.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                        }
                    }
                    StateMachine.State.IDLE -> {
                        val imageString = prefs.imageUriFlow(this@MainActivity).first()
                        if (imageString != null) {
                            val uri = Uri.parse(imageString)
                            imageController.load(uri)
                            binding.imageView.visibility = View.VISIBLE
                            binding.playerView.visibility = View.GONE
                        }
                        // Stop any video playback if we were playing
                        videoController.stop()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        triggerManager.start()
    }

    override fun onStop() {
        super.onStop()
        triggerManager.stop()
        stateJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoController.release()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Forward key events to trigger manager.  Consume if handled.
        if (triggerManager.onKeyEvent(event)) {
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    /** Puts the activity into sticky immersive mode hiding system bars. */
    private fun enterImmersiveMode() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    }
}
// NOTE: ensure VideoController.playPreloaded()/stopToStartFrame() used in state changes
