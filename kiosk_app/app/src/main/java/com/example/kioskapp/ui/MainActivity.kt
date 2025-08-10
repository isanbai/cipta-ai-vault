package com.example.kioskapp.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kioskapp.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayer.Builder(this).build().also { p ->
            binding.playerView.player = p
        }
    }

    override fun onStop() {
        binding.playerView.player = null
        player?.release()
        player = null
        super.onStop()
    }

    fun preloadAndPlay(uri: Uri, loop: Boolean) {
        val p = player ?: return
        p.setMediaItem(MediaItem.fromUri(uri))
        p.prepare()
        p.playWhenReady = true
        p.repeatMode = if (loop) com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
            else com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
        p.play()
    }
}
