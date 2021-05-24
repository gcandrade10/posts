package com.example.posts.repository.remote

import com.example.posts.BuildConfig
import com.example.posts.repository.persistence.PostEntity
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object ApiFactory {
    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url()
            .newBuilder()
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val builder = OkHttpClient().newBuilder().addInterceptor(authInterceptor)

    private fun retrofit(): Retrofit {
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(OkHttpProfilerInterceptor())
        }
        val client = builder.build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val api: Api = retrofit().create(Api::class.java)
}

interface Api {
    @GET("/posts")
    suspend fun getPosts(): Response<List<PostNet>>

    @GET("/posts/{id}/comments")
    suspend fun getComments(@Path("id") id: Int): Response<List<CommentNet>>

    @GET("/users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): Response<User>
}

data class PostNet(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

fun PostNet.toEntity() =
    PostEntity(
        id = id,
        userId = userId,
        title = title,
        body = body,
        isFavorite = false,
        isNew = true
    )

data class CommentNet(
    val body: String
)

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val website: String,
)