package com.example.myapplication.repository

import com.example.myapplication.repository.model.RickMortyResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RickMortyService {
    @GET("/api/character")
    suspend fun getRickMortyResponse(): Response<RickMortyResponse>

    companion object {
        private const val RICK_MORTY_URL = "https://rickandmortyapi.com/"

        private val logger =
            HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY }

        private val okHttp = OkHttpClient.Builder().apply {
            this.addInterceptor(logger) }.build()

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(RICK_MORTY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build()
        }

        val rickMortyService: RickMortyService by lazy {
            retrofit.create(RickMortyService::class.java)
        }
    }
}