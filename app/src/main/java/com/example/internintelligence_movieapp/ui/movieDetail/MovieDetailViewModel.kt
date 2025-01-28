package com.example.internintelligence_movieapp.ui.movieDetail

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.retrofit.Repository
import com.example.internintelligence_movieapp.retrofit.model.Movie
import com.example.internintelligence_movieapp.retrofit.model.MovieResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {
    private val repository = Repository()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    private val _movieDetails = MutableLiveData<Movie?>()
    val movieDetails: LiveData<Movie?>
        get() = _movieDetails

    private val _trailerUrl = MutableLiveData<String?>()
    val trailerUrl: LiveData<String?>
        get() = _trailerUrl


    //save
    fun addSaveToFirebase(postId: String) {
        val savedData = hashMapOf(
            postId to true
        )
        firebaseFirestore.collection("Saves").document(firebaseAuth.currentUser!!.uid)
            .set(savedData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("addSavedToFirestore", "Save added successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("addSavedToFirestore", "Error adding save: $exception")
            }
    }

    fun removeSaveFromFirestore(postId: String) {
        firebaseFirestore.collection("Saves").document(firebaseAuth.currentUser!!.uid)
            .update(postId, FieldValue.delete())
            .addOnSuccessListener {
                Log.d("removeSaveFromFirestore", "Save removed successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("removeSaveFromFirestore", "Error removing save: $exception")
            }
    }

    fun checkSaveStatus(postId: String, imageView: ImageView) {
        firebaseFirestore.collection("Saves").document(firebaseAuth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val savedPostId = document.getBoolean(postId) ?: false
                    if (savedPostId) {
                        imageView.setImageResource(R.drawable.ic_saved)
                        imageView.tag = "saved"
                    } else {
                        imageView.setImageResource(R.drawable.ic_save)
                        imageView.tag = "save"
                    }
                } else {
                    imageView.setImageResource(R.drawable.ic_save)
                    imageView.tag = "save"
                }
            }
            .addOnFailureListener { exception ->
                Log.e("checkSaveStatus", "Error checking save status: $exception")
            }
    }


    fun toggleSaveStatus(postId: String, imageView: ImageView) {
        val tag = imageView.tag?.toString() ?: ""

        if (tag == "saved") {
            imageView.setImageResource(R.drawable.ic_save)
            imageView.tag = "save"
            removeSaveFromFirestore(postId)
        } else {
            imageView.setImageResource(R.drawable.ic_saved)
            imageView.tag = "saved"
            addSaveToFirebase(postId)
        }
    }

    fun searchMovieByTitle(apiKey: String, title: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSearch(apiKey, title)
                if (response.isSuccessful) {
                    val movies = response.body()?.results
                    if (!movies.isNullOrEmpty()) {
                        _movieDetails.postValue(movies.first())
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


/*
    fun addVideoToFirestore(userId: String, title: String, url: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val userDoc = firebaseFirestore.collection("downloads").document(userId)

        val video = mapOf(
            "title" to title,
            "url" to url
        )

        userDoc.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val currentVideos = document.get("videos") as? ArrayList<Map<String, String>> ?: arrayListOf()
                currentVideos.add(video)
                userDoc.update("videos", currentVideos)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure("Videoyu yeniləmək mümkün olmadı: ${e.message}")
                    }
            } else {
                userDoc.set(mapOf("videos" to listOf(video)))
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure("Videonu əlavə etmək mümkün olmadı: ${e.message}")
                    }
            }
        }.addOnFailureListener { e ->
            onFailure("Firestore-dən sənəd oxunmadı: ${e.message}")
        }
    }
*/
}
