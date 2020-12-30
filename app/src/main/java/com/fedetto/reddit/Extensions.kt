package com.fedetto.reddit

import com.fedetto.arch.interfaces.ActionsDispatcher
import com.fedetto.reddit.arch.RedditAction
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.views.PostItem


fun List<Post>.mapToItems(
    bindingStrategy: PostBindingStrategy
) = map {
    PostItem(it,bindingStrategy)
}