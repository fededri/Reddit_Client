package com.fedetto.reddit.controllers

import com.fedetto.reddit.Globals
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.repositories.RedditRepository
import io.reactivex.Single
import javax.inject.Inject

class RedditController @Inject constructor(private val repository: RedditRepository) {

    private var after: String = ""

    fun getPosts(limit: Int): Single<List<Post>> {
        return loadPosts(limit)
    }

    private fun checkToken(): Single<String> {
        return if (Globals.ACCESS_TOKEN.isNullOrEmpty()) {
            repository.getAuthToken()
                .map {
                    Globals.ACCESS_TOKEN = it.accessToken
                    it.accessToken
                }
        } else {
            Single.just(Globals.ACCESS_TOKEN)
        }
    }


    fun loadMore(limit: Int): Single<List<Post>> {
        return loadPosts(limit, after)
    }

    private fun loadPosts(limit: Int, afterKey: String? = null): Single<List<Post>> {
        return checkToken()
            .flatMap {
                repository.getPosts(limit, afterKey)
                    .map {
                        after = it.info.after
                        it.info.children
                    }
            }
    }

}