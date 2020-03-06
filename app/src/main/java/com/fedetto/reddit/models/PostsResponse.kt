package com.fedetto.reddit.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PostsResponse(
    @SerializedName("data") val info: Data,
    val kind: String
)

data class Data(
    val after: String,
    val before: Any,
    val children: List<Post>,
    val dist: Int,
    val modhash: String
)

@Parcelize
data class Post(
    @SerializedName("data") val info: PostInfo,
    val kind: String
) : Parcelable

@Parcelize
data class PostInfo(
    val allow_live_comments: Boolean,
    val archived: Boolean,
    val author: String,

    val author_flair_type: String,
    val author_fullname: String,
    val author_patreon_flair: Boolean,
    val author_premium: Boolean,

    val can_mod_post: Boolean,

    val contest_mode: Boolean,
    val created: Int,
    val created_utc: Long,
    val domain: String,
    val downs: Int,
    val edited: Boolean,
    val gilded: Int,

    val hidden: Boolean,
    val hide_score: Boolean,
    val id: String,
    val is_crosspostable: Boolean,
    val is_meta: Boolean,
    val is_original_content: Boolean,
    val is_reddit_media_domain: Boolean,
    val is_robot_indexable: Boolean,
    val is_self: Boolean,
    val is_video: Boolean,

    val link_flair_text_color: String,
    val link_flair_type: String,
    val locked: Boolean,

    val media_only: Boolean,
    val name: String,
    val no_follow: Boolean,
    val num_comments: Int,
    val num_crossposts: Int,
    val over_18: Boolean,
    val parent_whitelist_status: String,
    val permalink: String,
    val pinned: Boolean,
    val post_hint: String,
    val pwls: Int,
    val quarantine: Boolean,

    val saved: Boolean,
    val score: Int,

    val selftext: String,
    val send_replies: Boolean,
    val spoiler: Boolean,
    val stickied: Boolean,
    val subreddit: String,
    val subreddit_id: String,
    val subreddit_name_prefixed: String,
    val subreddit_subscribers: Int,
    val subreddit_type: String,
    val thumbnail: String,
    val thumbnail_height: Int,
    val thumbnail_width: Int,
    val title: String,
    val total_awards_received: Int,
    val ups: Int,
    val url: String,
    val visited: Boolean,
    val whitelist_status: String,
    val wls: Int
) : Parcelable

