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
    object Refresh : RedditAction()
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
        val posts = controller.loadMore(pageSize).map {
            PostItem(it, actionsDispatcher, bindingStrategy)
        }
        return RedditAction.ShowPosts(posts)
    }

    private suspend fun loadPosts(actionsDispatcher: ActionsDispatcher<RedditAction>): RedditAction {
        val posts = controller.getPosts(pageSize).map {
            PostItem(it, actionsDispatcher, bindingStrategy)
        }
        return RedditAction.ShowPosts(posts)
    }
}

class RedditUpdater @Inject constructor() :
    Updater<RedditAction, RedditState, RedditSideEffect, RedditEvent> {

    override fun onNewAction(
        action: RedditAction,
        currentState: RedditState
    ): NextResult {
        return when (action) {
            is RedditAction.ShowPosts -> showPosts(currentState, action)
            is RedditAction.AppInitiated -> Next.StateWithSideEffects(
                currentState,
                setOf(RedditSideEffect.LoadPosts(action.actionsDispatcher))
            )
            is RedditAction.DismissPost -> dismissPost(action.post, currentState)
            is RedditAction.Refresh -> Next.State(currentState)
            is RedditAction.SelectPost -> selectPost(action.post, currentState)
            is RedditAction.DismissAll -> dismissAll(currentState)
            is RedditAction.LoadMorePosts -> loadMorePosts(currentState, action.actionsDispatcher)
        }
    }

    private fun showPosts(currentState: RedditState, action: RedditAction.ShowPosts): NextResult {
        val postsList = (currentState.posts ?: emptyList()).toMutableList().apply {
            addAll(action.posts)
        }
        return Next.State(currentState.copy(posts = postsList, loading = false))
    }

    private fun loadMorePosts(
        state: RedditState,
        actionsDispatcher: ActionsDispatcher<RedditAction>
    ): NextResult {
        return Next.StateWithSideEffects(
            state.copy(loading = true),
            setOf(RedditSideEffect.LoadMorePosts(actionsDispatcher))
        )
    }

    private fun dismissPost(
        post: Post,
        state: RedditState
    ): NextResult {
        val filteredPosts = state.posts?.filter {
            val item = it as? PostItem
            item?.post != post
        }
        return Next.State(state = state.copy(posts = filteredPosts))
    }

    private fun dismissAll(state: RedditState): NextResult {
        return Next.State(state.copy(posts = emptyList()))
    }

    private fun selectPost(
        post: Post,
        state: RedditState
    ): NextResult {
        return Next.StateWithEvents(
            state.copy(selectedPost = post),
            setOf(RedditEvent.OnPostSelected(post))
        )
    }
}

