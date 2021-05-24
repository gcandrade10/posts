package com.example.posts.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posts.R
import com.example.posts.databinding.FragmentPostDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PostDetailFragment : Fragment() {

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val postDetailViewModel: PostDetailViewModel by viewModel()

    private val args: PostDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpPostObserver()
        setUpCommentsObserver()
        setUpUserObserver()
        postDetailViewModel.getPost(args.id)
        postDetailViewModel.getComments(args.id)
    }

    private fun setUpToolbar() {
        binding.postsToolbar.setNavigationIconTint(Color.WHITE)
        NavigationUI.setupWithNavController(binding.postsToolbar, findNavController())
        binding.postsToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    postDetailViewModel.isFavorite(args.id)
                    true
                }
                else -> false
            }
        }
    }

    private fun setUpPostObserver() {
        postDetailViewModel.postLiveData.observe(viewLifecycleOwner) { post ->
            binding.placeItemDescription.text = post.body
            binding.postsToolbar.title = post.title
            val drawableInt =
                if (post.isFavorite) R.drawable.ic_baseline_star_24 else R.drawable.ic_baseline_star_border_24
            val drawable = ResourcesCompat.getDrawable(resources, drawableInt, null)
            binding.postsToolbar.menu.getItem(0).icon = drawable
            if (post.isNew) postDetailViewModel.setNotNew(args.id)
            postDetailViewModel.getUser(post.userId)
        }
    }

    private fun setUpCommentsObserver() {
        postDetailViewModel.commentsLiveData.observe(viewLifecycleOwner) { comments ->
            val adapter = CommentAdapter(comments)
            binding.comments.layoutManager = LinearLayoutManager(activity)
            binding.comments.adapter = adapter
        }
    }

    private fun setUpUserObserver() {
        postDetailViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            binding.userName.text = user.name
            binding.userEmail.text = user.email
            binding.userPhone.text = user.phone
            binding.userWebsite.text = user.website
        }

    }

}