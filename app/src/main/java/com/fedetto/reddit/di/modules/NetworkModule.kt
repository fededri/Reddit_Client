package com.fedetto.reddit.di.modules


import com.facebook.stetho.okhttp3.StethoInterceptor
import com.fedetto.reddit.di.qualifiers.BasicAuthClient
import com.fedetto.reddit.di.qualifiers.OAuthClient
import com.fedetto.reddit.di.scopes.AppScope
import com.fedetto.reddit.network.BasicAuthInterceptor
import com.fedetto.reddit.network.OAuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://www.reddit.com/api/v1/"
const val OAUTH_URL = "https://oauth.reddit.com/"


@Module
class NetworkModule {

    @Provides
    @AppScope
    fun gsonTypeAdapters(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @AppScope
    @OAuthClient
    fun okHttpClient(
        builder: OkHttpClient.Builder
    ): OkHttpClient {
        return builder.apply {
            addInterceptor(OAuthInterceptor())
            addNetworkInterceptor(StethoInterceptor())
        }.build()
    }

    @Provides
    @AppScope
    @BasicAuthClient
    fun okHttpClientBasic(
        builder: OkHttpClient.Builder
    ): OkHttpClient {

        return builder.apply {
            addInterceptor(BasicAuthInterceptor())
            addNetworkInterceptor(StethoInterceptor())
        }.build()
    }

    @Provides
    fun okHttpClientBuilder(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            readTimeout(5, TimeUnit.SECONDS)
            connectTimeout(5, TimeUnit.SECONDS)
        }
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }


    @Provides
    @AppScope
    @BasicAuthClient
    fun basicRetrofitClient(@BasicAuthClient httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @AppScope
    @OAuthClient
    fun oAuthRetrofitClient(@OAuthClient httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .baseUrl(OAUTH_URL)
            .build()
    }


}