package com.wawra.posts.presentation.deletedPosts

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


class DeletedPostsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private lateinit var viewModel: DeletedPostsViewModel
    private lateinit var postsAdapter: DeletedPostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(DeletedPostsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBarTitle(getString(R.string.deleted_post))
        setupAdapter()
        setupObservers()
        fragment_post_swipe_refresh.setOnRefreshListener { getDeletedPosts() }
    }

    override fun onResume() {
        super.onResume()
        getDeletedPosts()
    }

    private fun setupObservers() {
        viewModel.error.observe {
            navigate?.navigate(
                DeletedPostsFragmentDirections.toDialogError(getString(R.string.unknown_error, it))
            )
        }
        viewModel.posts.observe {
            fragment_post_swipe_refresh.isRefreshing = false
            postsAdapter.data = it
        }
    }

    private fun setupAdapter() {
        if (!::postsAdapter.isInitialized) postsAdapter = DeletedPostsAdapter(restoreCallBack)
        fragment_posts_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }
    }

    private fun getDeletedPosts() {
        fragment_post_swipe_refresh.isRefreshing = true
        viewModel.getPosts()
    }

    private val restoreCallBack: (Long) -> Unit = {
        // todo
        Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show()
    }

}