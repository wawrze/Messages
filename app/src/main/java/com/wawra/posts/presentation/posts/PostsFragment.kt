package com.wawra.posts.presentation.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wawra.posts.R
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

    private fun setupObservers() {
        viewModel.error.observe {
            navigate?.navigate(
                PostsFragmentDirections.toDialogError(getString(R.string.unknown_error, it))
            )
        }
    }

    private fun setupAdapter() {
        if (!::postsAdapter.isInitialized) {
            postsAdapter = PostsAdapter(this as PostActions)
            getPosts()
        }
        fragment_posts_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }
    }

    private fun getPosts() {
        fragment_post_swipe_refresh.isRefreshing = true
        viewModel.getPosts().observe {
            fragment_post_swipe_refresh.isRefreshing = false
            postsAdapter.data = it
        }
    }

    override fun details(postId: Long) {
        navigate?.navigate(PostsFragmentDirections.toFragmentPostDetails(postId))
    }

    override fun edit(postId: Long) {
        Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
    }

    override fun delete(postId: Long) {
        Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
    }

}