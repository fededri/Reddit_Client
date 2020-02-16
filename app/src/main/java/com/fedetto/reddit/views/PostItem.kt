package com.fedetto.reddit.views

import com.fedetto.reddit.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

class PostItem() : Item() {

    override fun getLayout(): Int {
        return R.layout.post_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
}