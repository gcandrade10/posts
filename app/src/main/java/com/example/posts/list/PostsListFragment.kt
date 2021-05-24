package com.example.posts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posts.NavGraphDirections
import com.example.posts.PostsFragmentViewModel
import com.example.posts.SwipeToDeleteCallback
import com.example.posts.databinding.FragmentPostsListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostsListFragment : Fragment() {

    private var _binding: FragmentPostsListBinding? = null
    private val binding get() = _binding!!
    private val model: PostsFragmentViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPostsObserver()
    }

    private fun setUpPostsObserver() {
        model.postsLiveData.observe(viewLifecycleOwner, { posts ->
            val adapter = PostAdapter(posts) {
                val action = NavGraphDirections.actionGlobalPostDetailFragment(it.id)
                NavHostFragment.findNavController(this).navigate(action)
            }
            val swipeHandler = object : SwipeToDeleteCallback(requireActivity()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val postViewHolder = viewHolder as PostViewHolder
                    model.removePost(postViewHolder.pPost.id)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(binding.postsList)
            binding.postsList.layoutManager = LinearLayoutManager(activity)
            binding.postsList.adapter = adapter
        })
    }
}