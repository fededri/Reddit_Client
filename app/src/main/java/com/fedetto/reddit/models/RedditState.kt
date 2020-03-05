package com.fedetto.reddit.models

import com.xwray.groupie.kotlinandroidextensions.Item

data class RedditState(
    val posts: List<Item>? = listOf(),
    val loading: Boolean = false,
    val isWaitingServiceResponse: Boolean = false,
    val wasFirstRequestCalled: Boolean = false
)