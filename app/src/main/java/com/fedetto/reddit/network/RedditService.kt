package com.fedetto.reddit.network

import com.fedetto.reddit.models.PostsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RedditService {

    @GET("top")
    fun getTopPosts(@Query("limit") limit: Int, @Query("after") after: String? = null, @Query("before") before: String? = null): Single<PostsResponse>
}