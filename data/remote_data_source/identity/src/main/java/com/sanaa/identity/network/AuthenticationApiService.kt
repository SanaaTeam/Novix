package com.sanaa.identity.network

import com.sanaa.identity.network.body.LoginPostBody
import com.sanaa.identity.network.response.CreateGuestSessionResponse
import com.sanaa.identity.network.response.CreateRequestTokenResponse
import com.sanaa.identity.network.response.CreateSessionResponse
import com.sanaa.identity.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationApiService {
    @GET("authentication/token/new")
    suspend fun createRequestToken(): Response<CreateRequestTokenResponse>

    @POST("authentication/token/validate_with_login")
    suspend fun login(
        @Body body: LoginPostBody,
    ): Response<LoginResponse>

    @POST("authentication/session/new")
    suspend fun createSession(@Body body: Map<String, String>): Response<CreateSessionResponse>

    @GET("authentication/guest_session/new")
    suspend fun createGuestSession(): Response<CreateGuestSessionResponse>

}