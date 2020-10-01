package com.wawra.posts.presentation.posts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wawra.posts.R
import com.wawra.posts.base.BaseAdapter
import com.wawra.posts.base.loadImage
import com.wawra.posts.base.setVisibility
import com.wawra.posts.database.entities.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostsAdapter(
    private val actions: PostActions
) : BaseAdapter<Post, PostsAdapter.PostViewHolder>() {

    override fun onBindViewHolder(holder: PostViewHolder, item: Post, position: Int) {
        holder.bindPost(item, actions, position == data.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
        parent.inflate(R.layout.item_post)
    )

    class PostViewHolder(itemView: View) : ViewHolder(itemView) {

        fun bindPost(post: Post, actions: PostActions, isLast: Boolean) = itemView.apply {
            item_post_title.text = post.title
            item_post_divider.setVisibility(!isLast)
            setOnClickListener { actions.details(post.postId) }
            item_post_delete_button.setOnClickListener { actions.delete(post.postId) }
            item_post_edit_button.setOnClickListener { actions.edit(post.postId) }
            item_post_icon.loadImage(post.iconUrl)
        }

    }

}