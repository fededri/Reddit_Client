package com.fedetto.reddit.di.modules

import com.fedetto.reddit.di.qualifiers.BasicAuthClient
import com.fedetto.reddit.di.qualifiers.OAuthClient
import com.fedetto.reddit.di.scopes.AppScope
import com.fedetto.reddit.network.AuthService
import com.fedetto.reddit.network.RedditService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RedditApiModule {

    @Provides
    @AppScope
    fun redditService(@OAuthClient retrofit: Retrofit): RedditService {
        return retrofit.create(RedditService::class.java)
    }

    @Provides
    fun authService(@BasicAuthClient retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}