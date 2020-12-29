package com.fedetto.reddit.views

import com.fedetto.arch.interfaces.ActionsDispatcher
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.R
import com.fedetto.reddit.arch.RedditAction
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.ViewAction
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.post_item.*
import kotlinx.coroutines.runBlocking

class PostItem(
    val post: Post,
    private val actionsDispatcher: ActionsDispatcher<RedditAction>?,
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
                runBlocking {
                    actionsDispatcher?.action(RedditAction.DismissPost(post))
                }
            }

            root.setOnClickListener {
                runBlocking {
                    actionsDispatcher?.action(RedditAction.SelectPost(post))
                }
            }

            //TODO read status
            bindingStrategy.bindCreationTime(post, textViewTime)
        }
    }
}