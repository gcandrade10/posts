package com.example.posts

import android.app.Application
import androidx.room.Room
import com.example.posts.detail.PostDetailViewModel
import com.example.posts.repository.Repository
import com.example.posts.repository.persistence.AppDataBase
import com.example.posts.repository.remote.ApiFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PostsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PostsApp)
            modules(repositoryModule)
        }

    }
}

val repositoryModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDataBase::class.java, "posts-db"
        ).build().postDao()
    }
    single { ApiFactory.api }
    single { Repository(get(), get()) }
    viewModel { PostDetailViewModel(get()) }
    viewModel { PostsFragmentViewModel(get()) }
}