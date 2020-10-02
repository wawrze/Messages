package com.wawra.posts.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_question.*

class ConfirmationDialogFragment : BaseDialog() {

    private val args by navArgs<ConfirmationDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_question, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog_question_header.text = args.message
        dialog_question_button_negative.setOnClickListener { super.dismiss() }
        dialog_question_button_positive.setOnClickListener {
            super.dismiss()
            (activity as? BaseActivity)?.confirmationDialogCallback?.invoke()
            (activity as? BaseActivity)?.confirmationDialogCallback = null
        }
    }

}