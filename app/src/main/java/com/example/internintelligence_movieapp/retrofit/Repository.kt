package com.example.internintelligence_movieapp.retrofit

import com.example.internintelligence_movieapp.retrofit.model.MovieResponse
import retrofit2.Response

class Repository {
    private val tmdbApi = RetrofitInstance.getInstance().create(TmdbService::class.java)

    suspend fun getPopularMovies(apiKey: String): Response<MovieResponse> {
        return tmdbApi.getPopularMovies(apiKey)
    }
}
