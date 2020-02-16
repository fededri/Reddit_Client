package com.fedetto.reddit.network

import retrofit2.http.GET

interface RedditService {

    @GET("/top")
    fun getTopPosts()
}