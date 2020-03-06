package com.fedetto.reddit.models

import com.fedetto.reddit.views.PostItem

sealed class ViewAction {
    data class SelectPost(val post: Post) : ViewAction()
    data class DismissPost(val post: PostItem) : ViewAction()
}