package com.fedetto.reddit.controllers

import com.fedetto.reddit.models.AuthTokenResponse
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.PostsResponse
import com.fedetto.reddit.repositories.RedditRepository
import io.reactivex.Single
import javax.inject.Inject

class RedditController @Inject constructor(private val repository: RedditRepository) {

    fun getPosts(limit: Int): Single<List<Post>> {
        return repository.getPosts(limit)
            .map {
                it.info.children
            }
    }

    fun getToken(): Single<AuthTokenResponse> {
        return repository.getAuthToken()
    }

}