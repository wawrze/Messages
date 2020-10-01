package com.wawra.posts.presentation.posts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wawra.posts.BuildConfig
import com.wawra.posts.R
import com.wawra.posts.base.BaseAdapter
import com.wawra.posts.base.loadImage
import com.wawra.posts.base.setVisibility
import com.wawra.posts.database.entities.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostsAdapter(
    private val detailsCallback: (Long) -> Unit,
    private val deleteCallback: (Long) -> Unit,
    private val editCallback: (Long) -> Unit
) : BaseAdapter<Post, PostsAdapter.PostViewHolder>() {

    override fun onBindViewHolder(holder: PostViewHolder, item: Post, position: Int) {
        holder.itemView.apply {
            item_post_title.text = item.title
            item_post_divider.setVisibility(position < itemCount - 1)
            setOnClickListener { detailsCallback.invoke(item.postId) }
            item_post_delete_button.setOnClickListener { deleteCallback.invoke(item.postId) }
            item_post_edit_button.setOnClickListener { editCallback.invoke(item.postId) }
            item_post_vertical_icon_background.visibility = View.GONE
            item_post_horizontal_icon_background.visibility = View.GONE
            post {
                val marginWidth = resources?.getDimensionPixelSize(
                    resources?.getIdentifier(
                        "margin_small",
                        "dimen",
                        BuildConfig.APPLICATION_ID
                    ) ?: 0
                ) ?: 0
                val maxImageWidth = holder.itemView.width - 2 * marginWidth
                context?.loadImage(
                    item.iconUrl,
                    item_post_horizontal_icon,
                    item_post_vertical_icon,
                    maxImageWidth
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostViewHolder(inflate(parent, R.layout.item_post))

    class PostViewHolder(itemView: View) : ViewHolder(itemView)

}