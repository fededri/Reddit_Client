package com.fedetto.reddit.views


import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.R
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.ViewAction
import io.reactivex.subjects.PublishSubject

@EpoxyModelClass(layout = R.layout.post_item)
abstract class PostItem : EpoxyModelWithHolder<PostItem.Holder>() {

    @EpoxyAttribute
    lateinit var post: Post
    @EpoxyAttribute
    lateinit var viewActions: PublishSubject<ViewAction>
    @EpoxyAttribute
    lateinit var bindingStrategy: PostBindingStrategy

    override fun bind(holder: Holder) {
        val info = post.info
        holder.apply {
            textViewTitle.text = info.title
            bindingStrategy.bindAuthor(post, textViewAuthor)
            textViewCommentsNumber.text = "${info.num_comments} comments"

            bindingStrategy.bindThumbnail(post, imageVieThumbnail)

            buttonDismiss.setOnClickListener {
                viewActions.onNext(ViewAction.DismissPost(this@PostItem))
            }

            rootView.setOnClickListener {
                viewActions.onNext(ViewAction.SelectPost(post))
            }

            //TODO read status
            bindingStrategy.bindCreationTime(post, textViewTime)
        }

    }

    class Holder : EpoxyHolder() {
        lateinit var textViewTitle: TextView
        lateinit var textViewCommentsNumber: TextView
        lateinit var textViewAuthor: TextView
        lateinit var textViewTime: TextView
        lateinit var imageVieThumbnail: ImageView
        lateinit var buttonDismiss: Button
        lateinit var rootView: View

        override fun bindView(itemView: View) {
            textViewTitle = itemView.findViewById(R.id.textViewTitle)
            textViewCommentsNumber = itemView.findViewById(R.id.textViewCommentsNumber)
            imageVieThumbnail = itemView.findViewById(R.id.imageVieThumbnail)
            textViewTime = itemView.findViewById(R.id.textViewTime)
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor)
            buttonDismiss = itemView.findViewById(R.id.buttonDismiss)
            rootView = itemView
        }
    }
}