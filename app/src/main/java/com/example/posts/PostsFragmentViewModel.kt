package com.example.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts.list.Post
import com.example.posts.repository.Repository
import com.example.posts.util.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PostsFragmentViewModel(private val repository: Repository) : ViewModel() {
    val postsLiveData = MutableLiveData<List<Post>>()
    val favoritePostsLiveData = MutableLiveData<List<Post>>()

    private val errorChannel = Channel<Unit>(Channel.BUFFERED)
    val errorFlow = errorChannel.receiveAsFlow()

    init {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            val either = repository.getPosts()
            if (either is Either.Right) {
                either.right.collect {
                    postsLiveData.postValue(it)
                }
            } else {
                errorChannel.send(Unit)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavoritePosts().collect {
                favoritePostsLiveData.postValue(it)
            }
        }
    }

    fun reset() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete()
            fetch()
        }
    }

    fun removePost(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.remove(id)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete()
        }
    }

}