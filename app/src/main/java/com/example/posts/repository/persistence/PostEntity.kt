package com.example.posts.repository.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.posts.list.Post

@Entity(tableName = "post")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userId: Int,
    val title: String,
    val body: String,
    val isFavorite: Boolean,
    val isNew: Boolean
)

fun PostEntity.toDomain() =
    Post(
        id = id,
        userId = userId,
        title = title,
        body = body,
        isFavorite = isFavorite,
        isNew = isNew
    )
