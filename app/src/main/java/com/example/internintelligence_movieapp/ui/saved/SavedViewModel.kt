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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedViewModel : ViewModel() {
    private val repository = Repository()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _savedPosts = MutableLiveData<Resource<List<String>>>()
    val savedPosts: LiveData<Resource<List<String>>> = _savedPosts

    private val _movieResult = MutableLiveData<Resource<List<Movie>>>()
    val movieResult: LiveData<Resource<List<Movie>>> = _movieResult



    init {
        fetchSavedMovies()
    }

    fun fetchSavedMovies() {
        _savedPosts.value = Resource.Loading

        firestore.collection("Saves").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val savedPostIds = document.data?.keys ?: emptySet<String>()
                    savedPostIds.forEach { id ->
                        //etchMoviesByIds("827c2738d945feb56a52ad0fc38dc665",id.toInt())
                        Log.e("TAGmoviesview", "fetchSavedMovies:$savedPostIds")
                        //fetchMoviesByIds(savedPostIds)
                    } }
                else {
                        _savedPosts.value = Resource.Success(emptyList())
                        _movieResult.value = Resource.Success(emptyList())
                    }
                }
                    .addOnFailureListener { exception ->
                        _savedPosts.value = Resource.Error(exception)
                        _movieResult.value = Resource.Error(exception)
                    }
            }

    /*fun fetchMoviesByIds(apikey:String,movieId:Int){
            viewModelScope.launch {
                try {
                    val response = repository.getMovieById(movieId,apikey)
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

    }*/

    /*private fun fetchMoviesByIds(savedPostIds: Set<String>) {
        val apiKey = "827c2738d945feb56a52ad0fc38dc665"
        val movieList = mutableListOf<Movie>()

        savedPostIds.forEach { movieId ->
            val id = movieId.toIntOrNull()  // Ensure the ID is valid
            if (id != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = repository.getMovieById(id, apiKey)
                        if (response.isSuccessful) {
                            Log.e("TAGmovie", "fetchMoviesByIds: $response", )
                            response.body()?.let { movieResponse ->
                                // `MovieResponse` içindən `results`-u alın
                                movieList.addAll(movieResponse.results)
                                Log.e("TAGmovie2", "fetchMoviesByIds: $movieList", )
                            }
                        } else {
                            Log.e("TMDB Error", "Failed to fetch movie with ID: $movieId")
                        }
                    } catch (e: Exception) {
                        Log.e("TMDB Error", "Error fetching movie data", e)
                    }
                }
            }
        }

        // Update the movieResult LiveData with fetched movie data
        _movieResult.postValue(Resource.Success(movieList))
    }*/
}
