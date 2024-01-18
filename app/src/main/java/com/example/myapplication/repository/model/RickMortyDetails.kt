package com.example.myapplication.repository.model

data class RickMortyDetails(
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val origin: Location,
    val location: Location,
    val episode: List<String>,
    var first: RickMortyDetailsEpisode
)

data class Location(
    val name: String,
    val url: String,
)

data class RickMortyDetailsEpisode(
    val name: String,
    val air_date: String,
    val episode: String,
)