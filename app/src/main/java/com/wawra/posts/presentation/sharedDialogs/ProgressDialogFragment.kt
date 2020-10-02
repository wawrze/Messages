package com.wawra.posts.presentation.sharedDialogs

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.BaseDialog

class ProgressDialogFragment : BaseDialog() {

    companion object {
        fun createAndShow(activity: Activity?) {
            (activity as? BaseActivity)?.supportFragmentManager?.let {
                ProgressDialogFragment().show(it, "")
            }
        }

        fun dismiss(activity: Activity?) {
            (activity as? BaseActivity)?.supportFragmentManager?.fragments
                ?.filterIsInstance<ProgressDialogFragment>()
                ?.forEach { it.dismissAllowingStateLoss() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_progress, container, false)

}