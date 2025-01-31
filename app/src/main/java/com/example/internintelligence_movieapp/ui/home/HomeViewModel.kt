package com.example.internintelligence_movieapp.ui.home

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.retrofit.Repository
import com.example.internintelligence_movieapp.retrofit.model.GenreResponse
import com.example.internintelligence_movieapp.retrofit.model.MovieResponse
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
class HomeViewModel : ViewModel() {

    private val repository = Repository()

    private val _popularMovieResult = MutableLiveData<MovieResponse?>()
    val popularMovieResult: LiveData<MovieResponse?>
        get() = _popularMovieResult
    private val _topRatedMovieResult = MutableLiveData<MovieResponse?>()
    val topRatedMovieResult: LiveData<MovieResponse?>
        get() = _topRatedMovieResult

    private val _nowPlayingMoviesResult = MutableLiveData<MovieResponse?>()
    val nowPlayingMoviesResult: LiveData<MovieResponse?>
        get() = _nowPlayingMoviesResult

    private val _movieGenres = MutableLiveData<GenreResponse?>()
    val movieGenres: LiveData<GenreResponse?>
        get() = _movieGenres

    private val _movieGenresMovies = MutableLiveData<MovieResponse?>()
    val movieGenresMovies: LiveData<MovieResponse?>
        get() = _movieGenresMovies

    fun getPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(apiKey)
                if (response.isSuccessful) {
                    _popularMovieResult.postValue(response.body())
                    Log.d("HomeViewModel", "Fetched popular movies: ${response.body()}")
                } else {
                    Log.e("HomeViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
                _popularMovieResult.postValue(null)
            }
        }
    }

    fun getTopRatedMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopRatedMovies(apiKey)
                if (response.isSuccessful) {
                    _topRatedMovieResult.postValue(response.body())
                    Log.d("HomeViewModel", "Fetched top rated movies: ${response.body()}")
                } else {
                    Log.e("HomeViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
                _topRatedMovieResult.postValue(null)
            }
        }
    }

    fun getNowPlayingMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getNowPlayingMovies(apiKey)
                if (response.isSuccessful) {
                    _nowPlayingMoviesResult.postValue(response.body())
                    Log.d("HomeViewModel", "Fetched now playing movies: ${response.body()}")
                } else {
                    Log.e("HomeViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
                _nowPlayingMoviesResult.postValue(null)
            }
        }
    }
    fun getGenres(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getGenres(apiKey)
                if (response.isSuccessful) {
                    _movieGenres.postValue(response.body())
                    Log.d("HomeViewModel", "Fetched now playing movies: ${response.body()}")
                } else {
                    Log.e("HomeViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
                _movieGenres.postValue(null)
            }
        }
    }
    fun getGenreMovies(apiKey: String, genreId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCategoryMovies(apiKey, genreId)
                if (response.isSuccessful) {
                    _movieGenresMovies.postValue(response.body())
                    Log.d("HomeViewModel", "Fetched genre movies: ${response.body()}")
                } else {
                    Log.e("HomeViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
                _movieGenresMovies.postValue(null)
            }
        }
    }


}
