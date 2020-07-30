package com.fedetto.reddit.models

import com.fedetto.reddit.views.PostItem_

data class RedditState(
    val posts: List<PostItem_>? = listOf(),
    val loading: Boolean = false,
    val isWaitingServiceResponse: Boolean = false,
    val wasFirstRequestCalled: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedPost: Post? = null
)