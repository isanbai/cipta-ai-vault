package com.example.kioskapp.player

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * Wraps an instance of [ExoPlayer] and exposes simple methods to prepare
 * playback of a video.  The caller must attach the player to a [PlayerView]
 * prior to starting playback.  Once prepared, the controller will honour
 * looping behaviour by setting the repeat mode appropriately.  When
 * `play()` is invoked the player begins playback and can be stopped via
 * [stop].  Always call [release] when finished using this controller to
 * avoid memory leaks.
 */
class VideoController(private val context: Context, private val playerView: PlayerView) {
    private var player: ExoPlayer? = null
    private var currentUri: Uri? = null

    /** Prepare the player with the given [Uri] and loop flag. */
    fun prepare(uri: Uri, loop: Boolean, mute: Boolean) {
        if (player == null) {
            player = ExoPlayer.Builder(context).build().also { exo ->
                playerView.player = exo
            }
        }
        val mediaItem = MediaItem.fromUri(uri)
        player?.setMediaItem(mediaItem)
        player?.repeatMode = if (loop) ExoPlayer.REPEAT_MODE_ALL else ExoPlayer.REPEAT_MODE_OFF
        player?.volume = if (mute) 0f else 1f
        player?.prepare()
        currentUri = uri
    }

    /** Start or resume playback if prepared. */
    fun play() {
        player?.playWhenReady = true
    }

    /** Stops playback and resets to the start of the media. */
    fun stop() {
        player?.pause()
        player?.seekTo(0)
    }

    /** Releases the underlying [ExoPlayer].  The controller becomes unusable after this call. */
    fun release() {
        player?.release()
        player = null
        currentUri = null
    }
}

// PATCH: Preload & instant start
private var preloaded = false
fun preload(uri: Uri, loop: Boolean, muted: Boolean) {
    ensurePlayer()
    if (!preloaded || currentUri != uri) {
        currentUri = uri
        player?.setMediaItem(MediaItem.fromUri(uri))
        player?.repeatMode = if (loop) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        player?.prepare()
        preloaded = true
    }
    setMuted(muted)
}

fun playPreloaded() {
    ensurePlayer()
    player?.seekTo(0)
    player?.playWhenReady = true
    player?.play()
}

fun stopToStartFrame() {
    player?.pause()
    player?.seekTo(0)
}
