package com.fedetto.reddit.models

import com.xwray.groupie.kotlinandroidextensions.Item

data class RedditState(
    val posts: List<Post>? = listOf(),
    val loading: Boolean = false,
    val isWaitingServiceResponse: Boolean = false,
    val wasFirstRequestCalled: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedPost: Post? = null,
    val currentPage: Int = 0
)