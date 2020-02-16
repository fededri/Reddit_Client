package com.fedetto.reddit.di.modules


import com.fedetto.reddit.di.scopes.AppScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://www.reddit.com/dev/api/"

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun gsonTypeAdapters(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @AppScope
    fun okHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            readTimeout(5, TimeUnit.SECONDS)
            connectTimeout(5, TimeUnit.SECONDS)
        }

        return builder.build()
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