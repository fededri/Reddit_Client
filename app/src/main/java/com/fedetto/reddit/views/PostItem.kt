package com.fedetto.reddit.views

import com.fedetto.reddit.R
import com.fedetto.reddit.models.Post
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.post_item.*

class PostItem(val post: Post) : Item() {

    override fun getLayout(): Int {
        return R.layout.post_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val info = post.info
        viewHolder.apply {
            textViewTitle.text = info.title
            textViewAuthor.text = info.author
            textViewTime.text = info.created_utc.toString()
            textViewCommentsNumber.text = "${info.num_comments} comments"

            //TODO read status
            //format time

        }
    }
}