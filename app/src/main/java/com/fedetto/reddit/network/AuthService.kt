package com.fedetto.reddit.network

import com.fedetto.reddit.models.AuthTokenResponse
import retrofit2.http.*

interface AuthService {

    @FormUrlEncoded
    @POST("access_token")
    suspend fun getAuthToken(@Field("grant_type") grantType: String, @Field("device_id") device: String): AuthTokenResponse
}