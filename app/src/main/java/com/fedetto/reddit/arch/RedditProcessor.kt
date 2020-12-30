package com.fedetto.reddit.arch

import com.fedetto.arch.interfaces.Processor
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.controllers.RedditController
import javax.inject.Inject

class RedditProcessor @Inject constructor(
    private val controller: RedditController
) :
    Processor<RedditSideEffect, RedditAction> {

    private val pageSize = 10

    override suspend fun dispatchSideEffect(effect: RedditSideEffect): RedditAction {
        return when (effect) {
            is RedditSideEffect.LoadPosts -> loadPosts()
            is RedditSideEffect.LoadMorePosts -> loadMorePosts()
        }
    }

    private suspend fun loadMorePosts(): RedditAction {
        return RedditAction.ShowPosts(controller.loadMore(pageSize))
    }

    private suspend fun loadPosts(): RedditAction {
        return RedditAction.ShowPosts(controller.getPosts(pageSize))
    }
}