package com.wawra.posts.base

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wawra.posts.BuildConfig

fun ImageView.loadImage(imageUrl: String) {
    val imageViewBackground = this.parent as CardView
    Glide.with(this.context)
        .asBitmap()
        .load(imageUrl)
        .into(
            object : CustomTarget<Bitmap?>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val imageHeight = resources?.getDimensionPixelSize(
                        resources?.getIdentifier(
                            "image_height",
                            "dimen",
                            BuildConfig.APPLICATION_ID
                        ) ?: 0
                    ) ?: 0
                    val bitmap = resource.resizeToHeight(imageHeight)
                    val params = imageViewBackground.layoutParams
                    params.height = bitmap.height
                    params.width = bitmap.width
                    imageViewBackground.layoutParams = params
                    this@loadImage.setImageBitmap(bitmap)
                }
            }
        )
}

fun Bitmap.resizeToHeight(height: Int): Bitmap {
    val divider = height.toDouble() / this.height
    return Bitmap.createScaledBitmap(
        this,
        (this.width * divider).toInt(),
        (this.height * divider).toInt(),
        false
    )
}

fun View.setVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}