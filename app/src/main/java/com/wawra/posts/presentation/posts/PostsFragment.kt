package com.wawra.posts.presentation.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.BaseFragment
import com.wawra.posts.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_posts.*
import javax.inject.Inject


class PostsFragment : BaseFragment(), PostActions {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private lateinit var viewModel: PostsViewModel
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBarTitle(getString(R.string.app_name))
        setupAdapter()
        setupObservers()
        fragment_post_swipe_refresh.setOnRefreshListener { getPosts() }
    }

    override fun onResume() {
        super.onResume()
        getPosts()
    }

    private fun setupObservers() {
        viewModel.error.observe {
            navigate?.navigate(
                PostsFragmentDirections.toDialogError(getString(R.string.unknown_error, it))
            )
        }
        viewModel.posts.observe {
            fragment_post_swipe_refresh.isRefreshing = false
            postsAdapter.data = it
        }
    }

    private fun setupAdapter() {
        if (!::postsAdapter.isInitialized) postsAdapter = PostsAdapter(this as PostActions)
        fragment_posts_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }
    }

    private fun getPosts() {
        fragment_post_swipe_refresh.isRefreshing = true
        viewModel.getPosts()
    }

    override fun details(postId: Long) {
        navigate?.navigate(PostsFragmentDirections.toFragmentPostDetails(postId))
    }

    override fun edit(postId: Long) {
        (activity as? BaseActivity)?.dialogCallback = { getPosts() }
        navigate?.navigate(PostsFragmentDirections.toFragmentPostEdit(postId))
    }

    override fun delete(postId: Long) {
        (activity as? BaseActivity)?.dialogCallback = { getPosts() }
        navigate?.navigate(PostsFragmentDirections.toDialogDelete(postId))
    }

}