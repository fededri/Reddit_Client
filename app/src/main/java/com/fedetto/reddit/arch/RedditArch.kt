package com.fedetto.reddit.arch

import com.fedetto.arch.Next
import com.fedetto.arch.Processor
import com.fedetto.arch.SideEffectInterface
import com.fedetto.arch.Updater
import com.fedetto.arch.interfaces.ActionsDispatcher
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.views.PostItem
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

typealias NextResult = Next<RedditState, RedditSideEffect, RedditEvent>

sealed class RedditAction {
    data class ShowPosts(val posts: List<Item>) : RedditAction()
    data class AppInitiated(val actionsDispatcher: ActionsDispatcher<RedditAction>) : RedditAction()
    data class Refresh(val actionsDispatcher: ActionsDispatcher<RedditAction>) : RedditAction()
    data class LoadMorePosts(val actionsDispatcher: ActionsDispatcher<RedditAction>) :
        RedditAction()

    data class DismissPost(val post: Post) : RedditAction()
    object DismissAll : RedditAction()
    data class SelectPost(val post: Post) : RedditAction()
}


sealed class RedditSideEffect(
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    override val coroutineScope: CoroutineScope? = null
) : SideEffectInterface {

    data class LoadPosts(val actionsDispatcher: ActionsDispatcher<RedditAction>) :
        RedditSideEffect()

    data class LoadMorePosts(val actionsDispatcher: ActionsDispatcher<RedditAction>) :
        RedditSideEffect()
}

sealed class RedditEvent {
    data class OnPostSelected(val post: Post) : RedditEvent()
}





