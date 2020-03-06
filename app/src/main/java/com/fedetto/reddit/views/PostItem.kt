package com.fedetto.reddit.views

import android.text.format.DateUtils
import com.bumptech.glide.Glide
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.R
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.ViewAction
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.post_item.*

class PostItem(
    private val post: Post,
    private val viewActions: PublishSubject<ViewAction>,
    private val bindingStrategy: PostBindingStrategy
) : Item() {


    override fun getLayout(): Int {
        return R.layout.post_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val info = post.info
        viewHolder.apply {
            textViewTitle.text = info.title
            bindingStrategy.bindAuthor(post, textViewAuthor)
            textViewCommentsNumber.text = "${info.num_comments} comments"

            bindingStrategy.bindThumbnail(post, imageVieThumbnail)

            buttonDismiss.setOnClickListener {
                viewActions.onNext(ViewAction.DismissPost(this@PostItem))
            }

            root.setOnClickListener {
                viewActions.onNext(ViewAction.SelectPost(post))
            }

            //TODO read status
            bindingStrategy.bindCreationTime(post, textViewTime)
        }
    }
}