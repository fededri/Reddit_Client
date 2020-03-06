package com.fedetto.reddit.di

import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.R
import com.fedetto.reddit.models.Post
import javax.inject.Inject

class PostBindingStrategyConcrete @Inject constructor() : PostBindingStrategy {

    override fun bindAuthor(post: Post, textView: TextView) {
        textView.text = post.info.author
    }

    override fun bindThumbnail(post: Post, imageView: ImageView) {
        Glide.with(imageView)
            .load(post.info.thumbnail)
            .centerCrop()
            .placeholder(R.drawable.thumbnail)
            .into(imageView)
    }

    override fun bindCreationTime(post: Post, textView: TextView) {
        textView.text = DateUtils.getRelativeTimeSpanString(post.info.created_utc * 1000)
    }

}