package com.fedetto.reddit.arch

import com.fedetto.arch.Next
import com.fedetto.arch.Updater
import com.fedetto.arch.interfaces.ActionsDispatcher
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.views.PostItem
import javax.inject.Inject

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
            is RedditAction.Refresh -> Next.StateWithSideEffects(
                currentState.copy(posts = emptyList()),
                setOf(RedditSideEffect.LoadPosts(action.actionsDispatcher))
            )
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