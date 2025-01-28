package com.example.internintelligence_movieapp.retrofit

import com.example.internintelligence_movieapp.retrofit.model.GenreResponse
import com.example.internintelligence_movieapp.retrofit.model.MovieResponse
import com.example.internintelligence_movieapp.retrofit.model.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getCategoryMovies(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int
    ): Response<MovieResponse>


    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>


    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): Response<GenreResponse>


    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<VideoResponse>


}
