package com.example.posts.repository.persistence

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [PostEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
