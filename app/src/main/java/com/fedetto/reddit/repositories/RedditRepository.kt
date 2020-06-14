package com.fedetto.reddit.repositories

import com.fedetto.reddit.models.AuthTokenResponse
import com.fedetto.reddit.models.Data
import com.fedetto.reddit.models.PostsResponse
import com.fedetto.reddit.network.AuthService
import com.fedetto.reddit.network.RedditService
import com.fedetto.reddit.room.Database
import com.fedetto.reddit.room.PostDao
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class RedditRepository @Inject constructor(
    private val redditService: RedditService,
    private val authService: AuthService,
    private val postsDao: PostDao
) {

    fun getPosts(limit: Int, after: String? = null): Single<PostsResponse> {
        return redditService.getTopPosts(limit, after = after)
            .doOnSuccess {
                postsDao.insertPosts(it.info.children)
            }
            .onErrorResumeNext {
                Single.just(PostsResponse(Data("", "", postsDao.getAllPosts(), 0, ""), ""))

            }
    }

    fun getAuthToken(): Single<AuthTokenResponse> {
        val deviceId = UUID.randomUUID().toString()
        val grantType = "https://oauth.reddit.com/grants/installed_client"
        return authService.getAuthToken(grantType, deviceId)
            .subscribeOn(Schedulers.io())
    }
}