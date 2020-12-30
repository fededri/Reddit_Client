package com.fedetto.reddit.arch

import com.fedetto.arch.Next
import com.fedetto.arch.interfaces.ActionsDispatcher
import com.fedetto.arch.interfaces.Updater
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
            is RedditAction.DismissPost -> dismissPost(action.post, currentState)
            is RedditAction.Refresh -> Next.StateWithSideEffects(
                currentState.copy(posts = emptyList()),
                setOf(RedditSideEffect.LoadPosts)
            )
            is RedditAction.SelectPost -> selectPost(action.post, currentState)
            is RedditAction.DismissAll -> dismissAll(currentState)
            is RedditAction.LoadMorePosts -> loadMorePosts(currentState)
        }
    }

    private fun showPosts(currentState: RedditState, action: RedditAction.ShowPosts): NextResult {
        val postsList = (currentState.posts ?: emptyList()).toMutableList().apply {
            addAll(action.posts)
        }
        return Next.State(currentState.copy(posts = postsList, loading = false))
    }

    private fun loadMorePosts(
        state: RedditState
    ): NextResult {
        return Next.StateWithSideEffects(
            state.copy(loading = true),
            setOf(RedditSideEffect.LoadMorePosts)
        )
    }

    private fun dismissPost(
        post: Post,
        state: RedditState
    ): NextResult {
        val filteredPosts = state.posts?.filter {
            it != post
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