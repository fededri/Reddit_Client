package com.fedetto.reddit.network


import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val clientId = "HyDkIrujoj9-8w"
        val credentials = Credentials.basic(clientId, "")
        val headers = request.headers.newBuilder()
            .add("Authorization", credentials)
            .build()
        request = request.newBuilder().headers(headers).build()
        return chain.proceed(request)
    }
}