package com.example.internintelligence_movieapp.retrofit

import com.example.internintelligence_movieapp.retrofit.model.GenreResponse
import com.example.internintelligence_movieapp.retrofit.model.MovieResponse
import retrofit2.Response

class Repository {
    private val tmdbApi = RetrofitInstance.getInstance().create(TmdbService::class.java)

    suspend fun getPopularMovies(apiKey: String): Response<MovieResponse> {
        return tmdbApi.getPopularMovies(apiKey)
    }

    suspend fun getTopRatedMovies(apiKey: String): Response<MovieResponse> {
        return tmdbApi.getTopRatedMovies(apiKey)
    }

    suspend fun getNowPlayingMovies(apiKey: String): Response<MovieResponse> {
        return tmdbApi.getNowPlayingMovies(apiKey)
    }

    suspend fun getCategoryMovies(apiKey: String, genreId: Int): Response<MovieResponse> {
        return tmdbApi.getCategoryMovies(apiKey, genreId)
    }
    suspend fun getGenres(apiKey: String): Response<GenreResponse> {
        return tmdbApi.getGenres(apiKey)
    }
}
