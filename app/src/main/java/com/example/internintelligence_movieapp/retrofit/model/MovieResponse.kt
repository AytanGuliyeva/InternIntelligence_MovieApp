package com.example.internintelligence_movieapp.retrofit.model

data class MovieResponse(
    val results: List<Movie>
)


data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val isSaved: Boolean = false
)


data class GenreResponse(
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String
)

