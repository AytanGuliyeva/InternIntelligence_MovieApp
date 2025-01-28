package com.example.internintelligence_movieapp.ui.movieDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internintelligence_movieapp.retrofit.Repository
import com.example.internintelligence_movieapp.retrofit.model.Movie
import com.example.internintelligence_movieapp.retrofit.model.MovieResponse
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {
    private val repository = Repository()

    private val _movieDetails = MutableLiveData<Movie?>()
    val movieDetails: LiveData<Movie?>
        get() = _movieDetails

    private val _trailerUrl = MutableLiveData<String?>()
    val trailerUrl: LiveData<String?>
        get() = _trailerUrl

    fun searchMovieByTitle(apiKey: String, title: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSearch(apiKey, title)
                if (response.isSuccessful) {
                    val movies = response.body()?.results
                    if (!movies.isNullOrEmpty()) {
                        _movieDetails.postValue(movies.first())
                        // Filmin ID-si ilə trailer məlumatını alın
                        val movieId = movies.first().id
                        getMovieTrailer(apiKey, movieId)
                    } else {
                        Log.e("Search", "No movies found for title: $title")
                    }
                } else {
                    Log.e("Search", "API response error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Search", "Error searching movie by title: $e")
            }
        }
    }

    private suspend fun getMovieTrailer(apiKey: String, movieId: Int) {
        try {
            val response = repository.getVideos(movieId, apiKey)
            if (response.isSuccessful) {
                val videos = response.body()?.results
                if (!videos.isNullOrEmpty()) {
                    val trailer = videos.first()
                    val videoUrl = "https://www.youtube.com/embed/${trailer.key}"
                    _trailerUrl.postValue(videoUrl)
                }
            } else {
                Log.e("Trailer", "API response error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("Trailer", "Error getting trailer: $e")
        }
    }

}
