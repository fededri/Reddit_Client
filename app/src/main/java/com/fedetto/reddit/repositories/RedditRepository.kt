package com.fedetto.reddit.repositories

import com.fedetto.reddit.models.AuthTokenResponse
import com.fedetto.reddit.models.Data
import com.fedetto.reddit.models.PostsResponse
import com.fedetto.reddit.network.AuthService
import com.fedetto.reddit.network.RedditService
import com.fedetto.reddit.room.PostDao
import java.util.*
import javax.inject.Inject


class RedditRepository @Inject constructor(
    private val redditService: RedditService,
    private val authService: AuthService,
    private val postsDao: PostDao
) {


    //Coroutine version
    suspend fun getPosts(limit: Int, after: String? = null): PostsResponse {
        return try {
            val response = redditService.getTopPostsSuspended(limit, after = after)
            postsDao.insertPosts(response.info.children)
            response
        } catch (e: Exception) {
            PostsResponse(Data("", "", postsDao.getAllPosts(), 0, ""), "")
        }
    }

    suspend fun getAuthToken(): AuthTokenResponse {
        val deviceId = UUID.randomUUID().toString()
        val grantType = "https://oauth.reddit.com/grants/installed_client"
        return authService.getAuthToken(grantType, deviceId)
    }
}