package com.example.internintelligence_movieapp.ui.saved

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internintelligence_movieapp.base.Resource
import com.example.internintelligence_movieapp.retrofit.Repository
import com.example.internintelligence_movieapp.retrofit.model.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val auth: FirebaseAuth
) : ViewModel() {
    private val repository = Repository()
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _movieResult = MutableLiveData<Resource<List<Movie>>>()
    val movieResult: LiveData<Resource<List<Movie>>> = _movieResult

    fun fetchSavedMovies(apikey: String) {
        _movieResult.value = Resource.Loading
        firestore.collection("Saves").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val savedPostIds = document.data?.keys?.map { it.toInt() } ?: emptyList()
                    if (savedPostIds.isNotEmpty()) {
                        Log.e("TAGsaved", "fetchMoviesByIds: $savedPostIds", )

                        fetchMoviesByIds(apikey, savedPostIds)
                    } else {
                        _movieResult.value = Resource.Success(emptyList())
                    }
                } else {
                    _movieResult.value = Resource.Success(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                _movieResult.value = Resource.Error(exception)
            }
    }
    private fun fetchMoviesByIds(apikey: String, movieIds: List<Int>) {
        val movies = mutableListOf<Movie>()
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            val results = movieIds.mapNotNull { movieId ->
                try {
                    val response = repository.getMovieById(movieId, apikey)
                    Log.e("TAGsaved1", "API Response: ${response.body()}")

                    Log.e("TAGsavedm", "fetchMoviesByIds: $movieId")

                    if (response.isSuccessful) {
                        Log.e("TAGsaved2", "API Response: ${response.body()}")

                        val movie = response.body()?.results?.firstOrNull()
                        if (movie != null) {
                            Log.e("TAGsaved", "Fetched movie: ${movie.title}")
                        } else {
                            Log.e("TAGsaved", "No movie found for ID: $movieId")
                        }
                        movie
                    } else {
                        Log.e("TAGsavedm", "Error fetching movie with ID $movieId: ${response.code()}")
                        null
                    }
                } catch (e: Exception) {
                    Log.e("SavedViewModel", "Error fetching movie with ID $movieId: ${e.message}")
                    null
                }
            }
            movies.addAll(results)

            if (movies.isEmpty()) {
                Log.e("TAGsaved", "No movies found for IDs: $movieIds")
            } else {
                Log.e("TAGsaved", "Movies fetched: ${movies.size}")
            }

            _movieResult.postValue(Resource.Success(movies))
        }
    }


    /*
        private fun fetchMoviesByIds(apikey: String, movieIds: List<Int>) {
            val movies = mutableListOf<Movie>()
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                movieIds.forEach { movieId ->
                    try {
                        val response = repository.getMovieById(movieId, apikey)
                        Log.e("TAGsavedm", "fetchMoviesByIds: $movieId", )

                        if (response.isSuccessful) {
                            response.body()?.results?.let {

                                movies.addAll(it)
                                Log.e("TAGsavedm", "fetchMoviesByIds: $movies", )
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SavedViewModel", "Error fetching movie: ${e.message}")
                    }
                }
                _movieResult.postValue(Resource.Success(movies))
            }
        }
    */
}
