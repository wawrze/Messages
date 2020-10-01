package com.wawra.posts.presentation.postDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.wawra.posts.BuildConfig
import com.wawra.posts.R
import com.wawra.posts.base.BaseFragment
import com.wawra.posts.base.ViewModelProviderFactory
import com.wawra.posts.base.loadImage
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
            view?.post {
                val smallMarginWidth = resources.getDimensionPixelSize(
                    resources.getIdentifier(
                        "margin_small",
                        "dimen",
                        BuildConfig.APPLICATION_ID
                    )
                )
                val largeMarginWidth = resources.getDimensionPixelSize(
                    resources.getIdentifier(
                        "margin_large",
                        "dimen",
                        BuildConfig.APPLICATION_ID
                    )
                )
                val maxImageWidth = (view?.width ?: 0) - 2 * smallMarginWidth
                context?.loadImage(
                    it.iconUrl,
                    fragment_post_details_horizontal_icon,
                    fragment_post_details_vertical_icon,
                    maxImageWidth
                ) {
                    val params =
                        ConstraintLayout.LayoutParams(fragment_post_details_divider.layoutParams)
                    params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    params.topMargin = largeMarginWidth
                    if (fragment_post_details_vertical_icon_background.visibility == View.GONE) {
                        params.topToBottom = R.id.fragment_post_details_title
                    } else {
                        params.topToBottom = R.id.fragment_post_details_vertical_icon_background
                    }
                    fragment_post_details_divider.layoutParams = params
                }
            }
        }
        viewModel.error.observe {
            Toast.makeText(context, getString(it), Toast.LENGTH_LONG).show()
        } // todo
    }

    private fun setupButtons() {
        fragment_post_details_edit_button.setOnClickListener {
            Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
        }
        fragment_post_details_delete_button.setOnClickListener {
            Toast.makeText(context, "NOT IMPLEMENTED!", Toast.LENGTH_LONG).show() // todo
        }
    }

}