package com.wawra.posts.presentation.postDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.BaseFragment
import com.wawra.posts.base.ViewModelProviderFactory
import com.wawra.posts.base.loadImage
import com.wawra.posts.presentation.posts.PostsFragmentDirections
import kotlinx.android.synthetic.main.fragment_post_details.*
import javax.inject.Inject

class PostDetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private lateinit var viewModel: PostDetailsViewModel
    private val args by navArgs<PostDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostDetailsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBarTitle(getString(R.string.post_details))
        setupButtons()
        setupObservers()
        viewModel.getPost(args.postId)
    }

    private fun setupObservers() {
        viewModel.post.observe {
            fragment_post_details_title.text = it.title
            fragment_post_details_details.text = it.description
            fragment_post_details_icon.loadImage(it.iconUrl)
        }
        viewModel.error.observe {
            navigate?.navigate(
                PostDetailsFragmentDirections.toDialogError(getString(R.string.unknown_error, it))
            )
        }
    }

    private fun setupButtons() {
        fragment_post_details_edit_button.setOnClickListener {
            (activity as? BaseActivity)?.dialogCallback = { viewModel.getPost(args.postId) }
            navigate?.navigate(PostsFragmentDirections.toFragmentPostEdit(args.postId))
        }
        fragment_post_details_delete_button.setOnClickListener {
            (activity as? BaseActivity)?.dialogCallback = { navigate?.navigateUp() }
            navigate?.navigate(PostsFragmentDirections.toDialogDelete(args.postId))
        }
    }

}