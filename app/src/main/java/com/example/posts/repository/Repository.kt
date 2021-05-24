package com.example.posts.repository

import com.example.posts.list.Post
import com.example.posts.repository.persistence.PostDao
import com.example.posts.repository.persistence.toDomain
import com.example.posts.repository.remote.Api
import com.example.posts.repository.remote.User
import com.example.posts.repository.remote.toEntity
import com.example.posts.util.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(private val api: Api, private val postDao: PostDao) {

    suspend fun getPosts(): Either<java.lang.Exception, Flow<List<Post>>> {
        if (postDao.count() == 0) {
            try {
                val result = api.getPosts()
                if (result.isSuccessful) {
                    val posts = result.body()?.map {
                        it.toEntity()
                    } ?: emptyList()
                    postDao.insert(posts)
                }
            } catch (e: Exception) {
                return Either.Left(e)
            }
        }
        return Either.Right(postDao.getPosts().map { list ->
            list.map {
                it.toDomain()
            }
        })
    }

    fun getPost(id: Int) = postDao.getPost(id)

    suspend fun getComments(id: Int): List<String> {
        return try {
            val result = api.getComments(id)
            if (result.isSuccessful) {
                result.body()?.map { it.body } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: java.lang.Exception) {
            emptyList()
        }
    }

    suspend fun getUser(userId: Int): User? {
        return try {
            val result = api.getUser(userId)
            if (result.isSuccessful) {
                result.body()
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
            null
        }
    }

    suspend fun isFavorite(id: Int) {
        postDao.setIsFavorite(id)
    }

    suspend fun setNotNew(id: Int) = postDao.setNotNew(id)

    fun getFavoritePosts(): Flow<List<Post>> {
        return postDao.getFavoritePosts().map { list ->
            list.map {
                it.toDomain()
            }
        }
    }

    suspend fun delete() {
        postDao.deleteAll()
    }

    suspend fun remove(id: Int) = postDao.remove(id)
}