package com.sanaa.identity.network

import com.sanaa.identity.network.body.LoginPostBody
import com.sanaa.identity.network.response.AccountDetailsResponse
import com.sanaa.identity.network.response.CreateGuestSessionResponse
import com.sanaa.identity.network.response.CreateRequestTokenResponse
import com.sanaa.identity.network.response.CreateSessionResponse
import com.sanaa.identity.network.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApiService {
    @GET("authentication/token/new")
    suspend fun createRequestToken(): CreateRequestTokenResponse

    @POST("authentication/token/validate_with_login")
    suspend fun login(
        @Body body: LoginPostBody,
    ): LoginResponse

    @POST("authentication/session/new")
    suspend fun createSession(@Body body: Map<String, String>): CreateSessionResponse

    @GET("authentication/guest_session/new")
    suspend fun createGuestSession(): CreateGuestSessionResponse

    @GET("account")
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): AccountDetailsResponse
}