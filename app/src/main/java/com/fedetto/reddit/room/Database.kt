package com.fedetto.reddit.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fedetto.reddit.models.Post

@Database(entities = arrayOf(Post::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun postsDao() : PostDao
}