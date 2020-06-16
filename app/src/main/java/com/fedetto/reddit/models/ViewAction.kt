package com.fedetto.reddit.models


sealed class ViewAction {
    data class SelectPost(val post: Post) : ViewAction()
    data class DismissPost(val post: Post) : ViewAction()
}