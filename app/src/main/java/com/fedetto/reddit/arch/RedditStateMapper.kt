package com.fedetto.reddit.arch

import com.fedetto.arch.interfaces.StateMapper
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.mapToItems
import com.fedetto.reddit.models.RedditState
import javax.inject.Inject

class RedditStateMapper @Inject constructor(val bindingStrategy: PostBindingStrategy) :
    StateMapper<RedditState, RenderState> {
    override fun mapToRenderState(state: RedditState): RenderState {
        return RenderState(
            posts = state.posts?.mapToItems(bindingStrategy),
            loading = state.loading,
            isRefreshing = state.isRefreshing,
            selectedPost = state.selectedPost
        )
    }
}