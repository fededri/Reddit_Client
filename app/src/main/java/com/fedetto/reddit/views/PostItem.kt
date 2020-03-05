package com.fedetto.reddit.views

import android.text.format.DateUtils
import com.bumptech.glide.Glide
import com.fedetto.reddit.R
import com.fedetto.reddit.models.Post
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.post_item.*

class PostItem(val post: Post, val dismissListener: (post: PostItem) -> Unit) : Item() {


    override fun getLayout(): Int {
        return R.layout.post_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val info = post.info
        viewHolder.apply {
            textViewTitle.text = info.title
            textViewAuthor.text = info.author
            textViewCommentsNumber.text = "${info.num_comments} comments"

            Glide.with(imageVieThumbnail)
                .load(info.thumbnail)
                .centerCrop()
                .placeholder(R.drawable.thumbnail)
                .into(imageVieThumbnail)

            buttonDismiss.setOnClickListener {
                dismissListener.invoke(this@PostItem)
            }

            //TODO read status
            textViewTime.text = DateUtils.getRelativeTimeSpanString(info.created_utc * 1000)
        }
    }
}