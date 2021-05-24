package com.example.posts.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts.repository.Repository
import com.example.posts.list.Post
import com.example.posts.repository.persistence.toDomain
import com.example.posts.repository.remote.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PostDetailViewModel(private val repository: Repository) : ViewModel() {

    val commentsLiveData = MutableLiveData<List<String>>()
    val postLiveData = MutableLiveData<Post>()
    val userLiveData = MutableLiveData<User>()

    fun getComments(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            commentsLiveData.postValue(repository.getComments(id))
        }
    }

    fun getUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUser(userId)?.let {
                userLiveData.postValue(it)
            }
        }
    }

    fun isFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.isFavorite(id)
        }
    }

    fun getPost(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPost(id).collect {
                postLiveData.postValue(it.toDomain())
            }
        }
    }

    fun setNotNew(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setNotNew(id)
        }
    }
}