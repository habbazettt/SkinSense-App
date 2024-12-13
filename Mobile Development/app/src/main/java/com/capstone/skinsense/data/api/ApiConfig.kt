package com.capstone.skinsense.data.api

import com.capstone.skinsense.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.IS_DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val client = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzM3MzE4MjcsImV4cCI6MTczNTAyNzgyN30.Xfa3Fc1FmT0I_ySSSVpnY6odQWyjLR2eABAk9k4v-ts"
                if (token != null) {
                    requestBuilder.addHeader("Cookie", "token=$token")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Menggunakan baseUrl dari BuildConfig
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
