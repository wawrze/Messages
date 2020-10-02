package com.wawra.posts.presentation.deleteDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.BaseDialog
import com.wawra.posts.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.dialog_question.*
import javax.inject.Inject

class DeleteDialogFragment : BaseDialog() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private lateinit var viewModel: DialogDeleteViewModel
    private val args by navArgs<DeleteDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(DialogDeleteViewModel::class.java)
        return inflater.inflate(R.layout.dialog_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog_question_header.text = getString(R.string.delete_question)
        setupButtons()
        setupObservers()
        viewModel.getPostTitle(args.postId)
    }

    private fun setupButtons() {
        dialog_question_button_positive.setOnClickListener { viewModel.deletePost(args.postId) }
        dialog_question_button_negative.setOnClickListener { super.dismiss() }
    }

    private fun setupObservers() {
        viewModel.postTitle.observe { dialog_question_message.text = it }
        viewModel.error.observe {
            super.dismiss()
            (activity as? BaseActivity)?.deleteDialogCallBack = null
            navigate?.navigate(
                R.id.dialog_error,
                bundleOf("message" to getString(R.string.unknown_error, it))
            )
        }
        viewModel.deleteResult.observe {
            super.dismiss()
            val message = getString(if (it) R.string.delete_confirmation else R.string.delete_error)
            navigate?.navigate(R.id.dialog_error, bundleOf("message" to message))
        }
    }

}