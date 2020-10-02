package com.wawra.posts.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.wawra.posts.R
import dagger.android.support.DaggerDialogFragment

abstract class BaseDialog : DaggerDialogFragment() {

    protected var navigate: NavController? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val margin = resources.getDimension(R.dimen.dialog_margin).toInt()
        dialog.window?.setBackgroundDrawable(
            InsetDrawable(ColorDrawable(Color.TRANSPARENT), margin)
        )
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        return dialog
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as android.view.WindowManager.LayoutParams
        dialog?.setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
        navigate = (activity as? Navigation)?.getNavigationController()
    }

    override fun onPause() {
        super.onPause()
        navigate = null
    }

    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }

    @MainThread
    protected fun <T> LiveData<T>.observe(action: (T) -> Unit) {
        this.observe(
            this@BaseDialog.viewLifecycleOwner,
            { action.invoke(it) }
        )
    }

}