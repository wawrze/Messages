package com.wawra.posts.presentation.deletedPosts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wawra.posts.R
import com.wawra.posts.base.BaseActivity
import com.wawra.posts.base.BaseFragment
import com.wawra.posts.base.ViewModelProviderFactory
import com.wawra.posts.logic.models.ErrorCodes
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
        viewModel.restoreResult.observe {
            if (it) {
                getDeletedPosts()
                navigate?.navigate(
                    R.id.dialog_error,
                    bundleOf("message" to getString(R.string.restore_confirmation))
                )
            } else {
                DeletedPostsFragmentDirections.toDialogError(
                    getString(
                        R.string.unknown_error,
                        ErrorCodes.DELETED_POSTS_VIEW_MODEL_RESTORE_POST_NOT_RESTORED.code
                    )
                )
            }
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
        viewModel.getDeletedPosts()
    }

    private val restoreCallBack: (Long) -> Unit = {
        (activity as? BaseActivity)?.confirmationDialogCallback = { viewModel.restorePost(it) }
        navigate?.navigate(
            DeletedPostsFragmentDirections.toDialogConfirmation(getString(R.string.restore_question))
        )
    }

}