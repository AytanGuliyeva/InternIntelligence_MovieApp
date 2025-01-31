package com.example.internintelligence_movieapp.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internintelligence_movieapp.retrofit.Repository
import com.example.internintelligence_movieapp.retrofit.model.MovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
class SearchViewModel:ViewModel() {
    private val repository = Repository()

    private val _movieResult = MutableLiveData<MovieResponse?>()
    val movieResult: LiveData<MovieResponse?>
        get() = _movieResult
    private val _searchResult = MutableLiveData<MovieResponse?>()
    val searchResult: LiveData<MovieResponse?>
        get() = _searchResult

    fun getMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMovies(apiKey)
                if (response.isSuccessful) {
                    _movieResult.postValue(response.body())
                    Log.d("HomeViewModel", "Fetched popular movies: ${response.body()}")
                } else {
                    Log.e("HomeViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
                _movieResult.postValue(null)
            }
        }
    }
    fun getSearch(apiKey: String, query: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSearch(apiKey, query)
                if (response.isSuccessful) {
                    _searchResult.postValue(response.body())
                    Log.d("SearchViewModel", "Fetched search results: ${response.body()}")
                } else {
                    Log.e("SearchViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Exception: ${e.message}")
                _searchResult.postValue(null)
            }
        }
    }

}