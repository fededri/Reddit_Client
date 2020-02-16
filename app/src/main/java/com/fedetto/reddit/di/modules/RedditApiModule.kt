package com.fedetto.reddit.di.modules

import com.fedetto.reddit.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RedditApiModule {

    @Provides
    @AppScope
    fun redditService(retrofit: Retrofit): RedditApiModule {
        return retrofit.create(RedditApiModule::class.java)
    }
}