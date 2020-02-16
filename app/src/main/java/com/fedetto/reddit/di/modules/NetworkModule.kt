package com.fedetto.reddit.di.modules


import com.fedetto.reddit.di.scopes.AppScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://www.reddit.com/dev/api"

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun gson(typeAdapters: @JvmSuppressWildcards Set<TypeAdapterFactory>): Gson {
        return GsonBuilder().apply {
            for (factory in typeAdapters) {
                this.registerTypeAdapterFactory(factory)
            }
        }.create()
    }

    @Provides
    @AppScope
    fun okHttpClient(
        httpClientBuilder: OkHttpClient.Builder
    ): OkHttpClient {
        return httpClientBuilder.build()
    }


    @Provides
    @AppScope
    fun retrofitClient(httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .baseUrl(BASE_URL)
            .build()
    }
}