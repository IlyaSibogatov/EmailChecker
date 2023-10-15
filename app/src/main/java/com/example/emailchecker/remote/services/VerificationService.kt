package com.example.emailchecker.remote.services

import com.example.emailchecker.remote.models.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VerificationService {
    @GET("verify?")
    suspend fun verifyEmail(
        @Query("email") email: String,
        @Query("api_key") apiKey: String,
    ): Response
}