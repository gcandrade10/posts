package com.example.posts.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.posts.databinding.PostItemBinding

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isFavorite: Boolean,
    val isNew: Boolean
)

class PostAdapter(
    private val posts: List<Post>, private val listener: (Post) -> Unit
) : RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemBinding =
            PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], listener)
    }

    override fun getItemCount() = posts.size

}


class PostViewHolder(private val itemBinding: PostItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    lateinit var pPost: Post

    fun bind(post: Post, listener: (Post) -> Unit) {
        pPost = post
        itemBinding.postItemDescription.text = post.title
        itemBinding.circle.isVisible = post.isNew
        itemBinding.star.isVisible = post.isFavorite
        itemBinding.root.setOnClickListener { listener(post) }
    }
}