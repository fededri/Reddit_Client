package com.fedetto.reddit.network

import com.fedetto.reddit.models.AuthTokenResponse
import io.reactivex.Single
import retrofit2.http.*

interface AuthService {

    @FormUrlEncoded
    @POST("access_token")
    fun getAuthToken(@Field("grant_type") grantType: String, @Field("device_id") device: String): Single<AuthTokenResponse>
}