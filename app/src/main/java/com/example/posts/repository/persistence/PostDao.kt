package com.example.posts.repository.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT COUNT(*) FROM post")
    fun count(): Int

    @Query("SELECT * from post")
    fun getPosts(): Flow<List<PostEntity>>

    @Query("SELECT * from post WHERE id= :id")
    fun getPost(id: Int): Flow<PostEntity>

    @Query("SELECT * from post WHERE isFavorite = 1")
    fun getFavoritePosts(): Flow<List<PostEntity>>

    @Query("UPDATE post SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun setIsFavorite(id: Int)

    @Query("UPDATE post SET isNew = 'false'WHERE id = :id")
    suspend fun setNotNew(id: Int)

    @Insert
    suspend fun insert(posts: List<PostEntity>)

    @Query("DELETE FROM post")
    suspend fun deleteAll()

    @Query("DELETE FROM post WHERE id = :id")
    suspend fun remove(id: Int)

}
