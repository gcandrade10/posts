package com.example.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.posts.databinding.FragmentPostsBinding
import com.example.posts.list.FavoritePostsListFragment
import com.example.posts.list.PostsListFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!
    private val model: PostsFragmentViewModel by viewModel()

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpErrorObserver()
        setUpViewPager()
        addToolbarListener(binding.postsToolbar)
        setUpDeleteButton()
    }

    private fun setUpDeleteButton() {
        binding.delete.setOnClickListener { model.deleteAll() }
    }

    private fun setUpErrorObserver() {
        model.errorFlow
            .onEach {
                Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_SHORT).show()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setUpViewPager() {
        val labels = listOf(getString(R.string.all), getString(R.string.favorites))
        val navHostFragmentAdapter = Adapter(requireActivity(), 2)
        binding.postsViewPager.adapter = navHostFragmentAdapter
        tabLayoutMediator =
            TabLayoutMediator(binding.postsTabLayout, binding.postsViewPager) { tab, position ->
                tab.text = (labels[position])
            }
        tabLayoutMediator.attach()
    }

    private fun addToolbarListener(toolbar: Toolbar) {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.refresh -> {
                    model.reset()
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        binding.postsViewPager.adapter = null
        tabLayoutMediator.detach()
        super.onDestroy()
    }
}


class Adapter(activity: FragmentActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostsListFragment()
            else -> FavoritePostsListFragment()
        }
    }
}