package com.example.kioskapp.image

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Handles image and GIF loading using the Glide library.  Glide will
 * automatically detect GIFs and play them in a looping fashion.  Static
 * images are cached and displayed efficiently.  Consumers should call
 * [load] whenever the image URI changes.  No explicit release method is
 * necessary because Glide handles lifecycle awareness when used with
 * activities.
 */
class ImageController(private val context: Context, private val imageView: ImageView) {
    /** Load the given [Uri] into the managed [ImageView]. */
    fun load(uri: Uri) {
        Glide.with(context)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}