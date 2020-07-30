package com.fedetto.reddit.controllers

import com.airbnb.epoxy.TypedEpoxyController
import com.fedetto.reddit.views.PostItem_

class EpoxyPostsController : TypedEpoxyController<List<PostItem_>>() {

    override fun buildModels(data: List<PostItem_>) {
        for (post in data) {
            post.addTo(this)
        }
    }
}