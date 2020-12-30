package com.fedetto.reddit.controllers

import android.util.Log
import androidx.annotation.WorkerThread
import com.fedetto.reddit.Globals
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.repositories.RedditRepository
import java.lang.Exception
import javax.inject.Inject

class RedditController @Inject constructor(private val repository: RedditRepository) {

    private var after: String = ""

    suspend fun getPosts(limit: Int): List<Post> {
        return loadPosts(limit)
    }

    private suspend fun checkToken(): String {
        return try {
            if (Globals.ACCESS_TOKEN.isNullOrEmpty()) {
                val token = repository.getAuthToken()
                Globals.ACCESS_TOKEN = token.accessToken
                token.accessToken
            } else {
                Globals.ACCESS_TOKEN.orEmpty()
            }
        } catch (e: Exception) {
            ""
        }
    }


    @WorkerThread
    suspend fun loadMore(limit: Int): List<Post> {
        return loadPosts(limit, after)
    }

    private suspend fun loadPosts(limit: Int, afterKey: String? = null): List<Post> {
        val threadName = Thread.currentThread().name
        Log.i("Controller", "running on thread $threadName")
        checkToken()
        val response = repository.getPosts(limit, afterKey)
        after = response.info.after
        return response.info.children
    }

}