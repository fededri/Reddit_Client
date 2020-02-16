package com.fedetto.reddit.di.modules

import com.fedetto.reddit.di.scopes.AppScope
import com.fedetto.reddit.network.RedditService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RedditApiModule {

    @Provides
    @AppScope
    fun redditService(retrofit: Retrofit): RedditService {
        return retrofit.create(RedditService::class.java)
    }
}