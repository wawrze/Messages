package com.wawra.posts.presentation.postEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.wawra.posts.R
import com.wawra.posts.base.BaseFragment
import com.wawra.posts.base.ViewModelProviderFactory
import com.wawra.posts.base.loadImage
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

    private fun setupObservers() {
        viewModel.post.observe {
            fragment_post_edit_title_input.setText(it.title)
            fragment_post_edit_content_input.setText(it.description)
            imageUrl = it.iconUrl
        }
        viewModel.error.observe {
            getString(R.string.unknown_error, it)
            // TODO
        }
    }

    private fun setupButtons() {
        fragment_post_edit_icon_edit.setOnClickListener {
            Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
        }
        fragment_post_edit_cancel_button.setOnClickListener {
            Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
        }
        fragment_post_edit_save_button.setOnClickListener {
            Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
        }
    }

}