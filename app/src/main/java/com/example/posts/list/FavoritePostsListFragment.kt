package com.example.posts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posts.NavGraphDirections
import com.example.posts.PostsFragmentViewModel
import com.example.posts.databinding.FragmentFavoritePostsListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoritePostsListFragment : Fragment() {

    private var _binding: FragmentFavoritePostsListBinding? = null
    private val binding get() = _binding!!
    private val model: PostsFragmentViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritePostsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFavoritePostsObserver()
    }

    private fun setUpFavoritePostsObserver() {
        model.favoritePostsLiveData.observe(viewLifecycleOwner, { posts ->
            val adapter = PostAdapter(posts) {
                val action = NavGraphDirections.actionGlobalPostDetailFragment(it.id)
                NavHostFragment.findNavController(this).navigate(action)
            }
            binding.favoritePostsList.layoutManager = LinearLayoutManager(activity)
            binding.favoritePostsList.adapter = adapter
        })
    }

}