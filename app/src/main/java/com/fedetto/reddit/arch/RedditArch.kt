package com.fedetto.reddit.arch

import com.fedetto.arch.Next
import com.fedetto.arch.interfaces.ActionsDispatcher
import com.fedetto.arch.interfaces.SideEffectInterface
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.RedditState
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

typealias NextResult = Next<RedditState, RedditSideEffect, RedditEvent>

sealed class RedditAction {
    data class ShowPosts(val posts: List<Post>) : RedditAction()
    data class Refresh(val actionsDispatcher: ActionsDispatcher<RedditAction>) : RedditAction()
    object LoadMorePosts :
        RedditAction()

    data class DismissPost(val post: Post) : RedditAction()
    object DismissAll : RedditAction()
    data class SelectPost(val post: Post) : RedditAction()
}


sealed class RedditSideEffect(
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    override val coroutineScope: CoroutineScope? = null
) : SideEffectInterface {

    object LoadPosts :
        RedditSideEffect()

    object LoadMorePosts :
        RedditSideEffect()
}

sealed class RedditEvent {
    data class OnPostSelected(val post: Post) : RedditEvent()
}

data class RenderState(
    val posts: List<Item>? = emptyList(),
    val loading: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedPost: Post? = null,
)



