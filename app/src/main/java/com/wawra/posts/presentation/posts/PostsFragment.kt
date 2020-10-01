package com.wawra.posts.presentation.posts

import android.os.Bundle
import android.os.Parcelable
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


class PostsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private lateinit var viewModel: PostsViewModel
    private lateinit var postsAdapter: PostsAdapter
    private var state: Parcelable? = null

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
        fragment_post_swipe_refresh.isRefreshing = true
        fragment_post_swipe_refresh.setOnRefreshListener { viewModel.getPosts() }
        viewModel.getPosts()
    }

    private fun setupObservers() {
        viewModel.posts.observe {
            fragment_post_swipe_refresh.isRefreshing = false
            postsAdapter.data = it
        }
        viewModel.error.observe {
            Toast.makeText(context, getString(it), Toast.LENGTH_LONG).show()
        } // todo
    }

    private fun setupAdapter() {
        postsAdapter = PostsAdapter(showPostDetailsCallback, deletePostCallback, editPostCallback)
        fragment_posts_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }
    }

    private val showPostDetailsCallback: (Long) -> Unit = {
        navigate?.navigate(PostsFragmentDirections.toFragmentPostDetails(it))
    }

    private val deletePostCallback: (Long) -> Unit = {
        Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
    }

    private val editPostCallback: (Long) -> Unit = {
        Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
    }

}