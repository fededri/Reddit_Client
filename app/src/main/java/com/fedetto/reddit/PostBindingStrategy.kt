package com.fedetto.reddit

import android.widget.ImageView
import android.widget.TextView
import com.fedetto.reddit.models.Post

interface PostBindingStrategy {
    fun bindAuthor(post: Post, textView: TextView)

    fun bindThumbnail(post: Post, imageView: ImageView)

    fun bindCreationTime(post: Post, textView: TextView)
}