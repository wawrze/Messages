package com.wawra.posts.presentation.postEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.BaseFragment
import com.wawra.posts.base.ViewModelProviderFactory
import com.wawra.posts.base.loadImage
import com.wawra.posts.logic.models.ErrorCodes
import kotlinx.android.synthetic.main.fragment_post_edit.*
import javax.inject.Inject

class PostEditFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private lateinit var viewModel: PostEditViewModel
    private val args by navArgs<PostEditFragmentArgs>()
    private var imageUrl = ""
        set(value) {
            field = value
            fragment_post_edit_icon.loadImage(value)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostEditViewModel::class.java)
        return inflater.inflate(R.layout.fragment_post_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        setupObservers()
        if (args.postId > 0L) {
            setTopBarTitle(getString(R.string.post_edit))
            viewModel.getPost(args.postId)
        } else {
            setTopBarTitle(getString(R.string.new_post))
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.imageUrlCallBack = { imageUrl = it }
    }

    override fun onPause() {
        super.onPause()
        (activity as? BaseActivity)?.imageUrlCallBack = null
    }

    private fun setupObservers() {
        viewModel.post.observe {
            fragment_post_edit_title_input.setText(it.title)
            fragment_post_edit_content_input.setText(it.description)
            imageUrl = it.iconUrl
        }
        viewModel.error.observe {
            navigate?.navigate(
                PostEditFragmentDirections.toDialogError(getString(R.string.unknown_error, it))
            )
        }
        viewModel.saveResult.observe {
            if (it > 0L) {
                navigate?.navigate(PostEditFragmentDirections.toFragmentPosts())
                navigate?.navigate(
                    R.id.fragment_post_details,
                    bundleOf("post_id" to it)
                )
            } else {
                PostEditFragmentDirections.toDialogError(
                    getString(
                        R.string.unknown_error,
                        ErrorCodes.POST_EDIT_VIEW_MODEL_SAVE_POST_NOT_SAVED.code
                    )
                )
            }
        }
    }

    private fun setupButtons() {
        fragment_post_edit_icon_edit.setOnClickListener {
            navigate?.navigate(PostEditFragmentDirections.toDialogImageUrl(imageUrl))
        }
        fragment_post_edit_cancel_button.setOnClickListener {
            (activity as? BaseActivity)?.confirmationDialogCallback = {
                navigate?.navigateUp()
                navigate?.navigateUp()
            }
            navigate?.navigate(
                PostEditFragmentDirections.toDialogConfirmation(
                    getString(R.string.cancel_edit_confirmation)
                )
            )
        }
        fragment_post_edit_save_button.setOnClickListener {
            (activity as? BaseActivity)?.confirmationDialogCallback = {
                val title = fragment_post_edit_title_input.text.toString()
                val content = fragment_post_edit_content_input.text.toString()
                viewModel.savePost(args.postId, title, content, imageUrl)
            }
            navigate?.navigate(
                PostEditFragmentDirections.toDialogConfirmation(
                    getString(R.string.save_confirmation)
                )
            )
        }
    }

}