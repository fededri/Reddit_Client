package com.fedetto.reddit.arch

import com.fedetto.arch.Processor
import com.fedetto.arch.interfaces.ActionsDispatcher
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.views.PostItem
import javax.inject.Inject

class RedditProcessor @Inject constructor(
    private val controller: RedditController,
    private val bindingStrategy: PostBindingStrategy
) :
    Processor<RedditSideEffect, RedditAction> {

    private val pageSize = 10

    override suspend fun dispatchSideEffect(effect: RedditSideEffect): RedditAction {
        return when (effect) {
            is RedditSideEffect.LoadPosts -> loadPosts(effect.actionsDispatcher)
            is RedditSideEffect.LoadMorePosts -> loadMorePosts(effect.actionsDispatcher)
        }
    }

    private suspend fun loadMorePosts(actionsDispatcher: ActionsDispatcher<RedditAction>): RedditAction {
        return RedditAction.ShowPosts(controller.loadMore(pageSize).mapToItems(actionsDispatcher))
    }

    private suspend fun loadPosts(actionsDispatcher: ActionsDispatcher<RedditAction>): RedditAction {
        return RedditAction.ShowPosts(controller.getPosts(pageSize).mapToItems(actionsDispatcher))
    }

    private fun List<Post>.mapToItems(actionsDispatcher: ActionsDispatcher<RedditAction>) = map {
        PostItem(it, actionsDispatcher, bindingStrategy)
    }
}