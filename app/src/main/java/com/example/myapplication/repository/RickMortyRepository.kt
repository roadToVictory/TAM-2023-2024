package com.example.myapplication.repository

import com.example.myapplication.repository.model.RickMortyDetails
import com.example.myapplication.repository.model.RickMortyDetailsEpisode
import com.example.myapplication.repository.model.RickMortyResponse
import retrofit2.Response

class RickMortyRepository {
    suspend fun getRickMortyResponse(): Response<RickMortyResponse> =
        RickMortyService.rickMortyService.getRickMortyResponse()

    suspend fun getRickMortyDetailsResponse(id: String): Response<RickMortyDetails> =
        RickMortyService.rickMortyService.getRickMortyDetailsResponse(id)

    suspend fun getRickMortyDetailsEpisodeResponse(id: String): Response<RickMortyDetailsEpisode> =
        RickMortyService.rickMortyService.getRickMortyDetailsEpisodeResponse(id)
}