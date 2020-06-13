package com.fedetto.reddit.di.modules

import android.app.Application
import androidx.room.Room
import com.fedetto.reddit.room.Database
import com.fedetto.reddit.room.PostDao
import dagger.Module
import dagger.Provides

@Module
class RoomModule {
    @Provides
    fun provideDatabase(context: Application): Database {
        return Room.databaseBuilder(context, Database::class.java, "database-name").build()
    }

    @Provides
    fun providePostsDao(database: Database) : PostDao {
        return database.postsDao()
    }
}