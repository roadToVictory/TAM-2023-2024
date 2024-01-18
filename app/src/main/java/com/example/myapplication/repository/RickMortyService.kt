package com.example.myapplication.repository

import com.example.myapplication.repository.model.RickMortyDetails
import com.example.myapplication.repository.model.RickMortyDetailsEpisode
import com.example.myapplication.repository.model.RickMortyResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RickMortyService {
    @GET("/api/character")
    suspend fun getRickMortyResponse(): Response<RickMortyResponse>

    @GET("/api/character/{id}")
    suspend fun getRickMortyDetailsResponse(@Path("id") id: String): Response<RickMortyDetails>

    @GET("/api/episode/{id}")
    suspend fun getRickMortyDetailsEpisodeResponse(@Path("id") id: String): Response<RickMortyDetailsEpisode>

    companion object {
        private const val RICK_MORTY_URL = "https://rickandmortyapi.com/"

        private val logger =
            HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

        private val okHttp = OkHttpClient.Builder().apply {
            this.addInterceptor(logger)
        }.build()

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