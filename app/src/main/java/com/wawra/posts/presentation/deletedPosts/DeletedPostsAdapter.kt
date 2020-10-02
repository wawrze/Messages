package com.wawra.posts.presentation.deletedPosts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wawra.posts.R
import com.wawra.posts.base.BaseAdapter
import com.wawra.posts.base.loadImage
import com.wawra.posts.base.setVisibility
import com.wawra.posts.database.entities.Post
import kotlinx.android.synthetic.main.item_deleted_post.view.*

class DeletedPostsAdapter(
    private val restoreCallBack: ((Long) -> Unit)?
) : BaseAdapter<Post, DeletedPostsAdapter.DeletedPostViewHolder>() {

    override fun onBindViewHolder(holder: DeletedPostViewHolder, item: Post, position: Int) {
        holder.bindPost(item, restoreCallBack, position == data.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeletedPostViewHolder(
        parent.inflate(R.layout.item_deleted_post)
    )

    class DeletedPostViewHolder(itemView: View) : ViewHolder(itemView) {

        fun bindPost(post: Post, restoreCallBack: ((Long) -> Unit)?, isLast: Boolean) {
            itemView.apply {
                item_deleted_post_title.text = post.title
                item_deleted_post_divider.setVisibility(!isLast)
                item_deleted_post_restore_button.setOnClickListener {
                    restoreCallBack?.invoke(post.postId)
                }
                item_deleted_post_icon.loadImage(post.iconUrl)
            }
        }

    }

}