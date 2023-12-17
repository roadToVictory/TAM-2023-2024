package com.example.myapplication.repository

import com.example.myapplication.repository.model.RickMortyResponse
import retrofit2.Response

class RickMortyRepository {
    suspend fun getRickMortyResponse(): Response<RickMortyResponse> =
        RickMortyService.rickMortyService.getRickMortyResponse()
}