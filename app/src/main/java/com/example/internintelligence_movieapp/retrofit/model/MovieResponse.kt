package com.example.internintelligence_movieapp.retrofit.model

data class MovieResponse(
    val results: List<Movie>
)

// Tek bir film modeli
data class Movie(
    val title: String,
    val poster_path: String,
    val overview: String
)