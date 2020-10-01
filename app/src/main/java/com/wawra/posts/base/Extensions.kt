package com.wawra.posts.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.wawra.posts.BuildConfig

fun Context.loadImage(
    imageUrl: String,
    horizontalImageView: ImageView,
    verticalImageView: ImageView,
    maxWidth: Int,
    callBack: (() -> Unit)? = null
) {
    val horizontalImageViewBackground = horizontalImageView.parent as CardView
    val verticalImageViewBackground = (verticalImageView.parent as CardView)
    horizontalImageViewBackground.visibility = View.GONE
    verticalImageViewBackground.visibility = View.GONE
    Glide.with(this)
        .asBitmap()
        .load(imageUrl)
        .into(
            object : CustomTarget<Bitmap?>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val bitmap: Bitmap
                    val view = if (resource.width > resource.height) {
                        bitmap = resource.resizeToWidth(maxWidth)
                        horizontalImageViewBackground.visibility = View.VISIBLE
                        horizontalImageView
                    } else {
                        val maxImageHeight = resources?.getDimensionPixelSize(
                            resources?.getIdentifier(
                                "image_max_height",
                                "dimen",
                                BuildConfig.APPLICATION_ID
                            ) ?: 0
                        ) ?: 0
                        bitmap = resource.resizeToHeight(maxImageHeight)
                        verticalImageViewBackground.visibility = View.VISIBLE
                        verticalImageView
                    }
                    val params = (view.parent as CardView).layoutParams
                    params.height = bitmap.height
                    params.width = bitmap.width
                    (view.parent as CardView).layoutParams = params
                    view.setImageBitmap(bitmap)
                    callBack?.invoke()
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

fun Bitmap.resizeToWidth(width: Int): Bitmap {
    val divider = width.toDouble() / this.width
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