package com.fedetto.reddit.network

import com.fedetto.reddit.models.PostsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {

    @GET("top")
    suspend fun getTopPostsSuspended(
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): PostsResponse
}