package com.example.internintelligence_movieapp.retrofit.model

data class MovieResponse(
    val results: List<Movie>
)

data class Movie(
    val title: String,
    val poster_path: String,
    val overview: String
)

data class GenreResponse(
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String
)

