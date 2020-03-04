package com.fedetto.reddit.controllers

import com.fedetto.reddit.models.AuthTokenResponse
import com.fedetto.reddit.repositories.RedditRepository
import io.reactivex.Single
import javax.inject.Inject

class RedditController @Inject constructor(private val repository: RedditRepository) {

    fun getPosts(limit: Int): Single<Any> {
        return repository.getPosts(limit)
    }

    fun getToken(): Single<AuthTokenResponse> {
        return repository.getAuthToken()
    }

}