package com.fedetto.reddit.network

import com.fedetto.reddit.Globals
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

class OAuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val headers: Headers = request.headers.newBuilder().add("Authorization", "Bearer ${Globals.ACCESS_TOKEN}").build()
        request = request.newBuilder().headers(headers).build()
        return chain.proceed(request)
    }
}