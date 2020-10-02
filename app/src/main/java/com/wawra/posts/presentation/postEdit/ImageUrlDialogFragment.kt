package com.wawra.posts.presentation.postEdit

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.BaseDialog
import com.wawra.posts.logic.models.ErrorCodes
import com.wawra.posts.presentation.sharedDialogs.ProgressDialogFragment
import kotlinx.android.synthetic.main.dialog_image_url.*


class ImageUrlDialogFragment : BaseDialog() {

    private val args by navArgs<ImageUrlDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_image_url, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog_image_url_input.setText(args.imageUrl)
        dialog_image_url_button_negative.setOnClickListener { super.dismiss() }
        dialog_image_url_button_positive.setOnClickListener { loadPhoto() }
    }

    private fun loadPhoto() {
        ProgressDialogFragment.createAndShow(activity)
        val url = dialog_image_url_input.text.toString()
        activity?.let {
            Glide.with(it)
                .load(url)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        it.runOnUiThread { showInvalidUrlError() }
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ProgressDialogFragment.dismiss(activity)
                        super@ImageUrlDialogFragment.dismiss()
                        (activity as? BaseActivity)?.imageUrlCallBack?.invoke(url)
                        return true
                    }

                })
                .submit()
        } ?: navigate?.navigate(
            ImageUrlDialogFragmentDirections.toDialogError(
                getString(R.string.unknown_error, ErrorCodes.IMAGE_URL_DIALOG_NO_ACTIVITY.code)
            )
        )
    }

    private fun showInvalidUrlError() {
        ProgressDialogFragment.dismiss(activity)
        navigate?.navigate(
            ImageUrlDialogFragmentDirections.toDialogError(getString(R.string.image_invalid_url))
        )
    }

}