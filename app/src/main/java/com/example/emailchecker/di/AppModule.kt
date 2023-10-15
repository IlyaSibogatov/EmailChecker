package com.example.emailchecker.di

import android.util.Log
import com.example.emailchecker.remote.services.VerificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun baseUrl() = "https://api.kickbox.com/v2/"

    @Singleton
    @Provides
    fun provideVerificationService(): VerificationService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl())
            .client(getOkHttpClient())
            .build()
        return retrofit.create(VerificationService::class.java)
    }

    private fun getOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor { message ->
                    Log.d("OK HTTP", message)
                }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
}