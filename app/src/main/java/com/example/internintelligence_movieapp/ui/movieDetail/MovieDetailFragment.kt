package com.example.internintelligence_movieapp.ui.movieDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.databinding.FragmentMovieDetailBinding
import com.example.internintelligence_movieapp.retrofit.model.Movie
import com.example.internintelligence_movieapp.ui.search.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding
    val args: MovieDetailFragmentArgs by navArgs()
    val viewModel: MovieDetailViewModel by viewModels()
    lateinit var auth: FirebaseAuth
    private var videoUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = Firebase.auth
        val apiKey = "827c2738d945feb56a52ad0fc38dc665"

        viewModel.searchMovieByTitle(apiKey, args.title)

        observeMovieDetails()
        observeTrailerUrl()

        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeMovieDetails() {
        viewModel.movieDetails.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                populateMovieDetails(movie)
            } else {
                binding.textTitle.text = "No movie found"
            }
        }
    }

    private fun observeTrailerUrl() {
        viewModel.trailerUrl.observe(viewLifecycleOwner) { trailerUrl ->
            if (trailerUrl != null) {
                binding.buttonPlay.setOnClickListener {
                    videoUrl = trailerUrl
                    playTrailer(trailerUrl)


                }
            } else {
                Toast.makeText(requireContext(), "No trailer found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateMovieDetails(movie: Movie) {
        viewModel.checkSaveStatus(movie.id.toString(),binding.iconSave)
        binding.textTitle.text = movie.title
        binding.textDescription.text = movie.overview
        binding.textDescription2.text = movie.overview
        binding.textYear.text = movie.release_date
        binding.iconSave.setOnClickListener {
            viewModel.toggleSaveStatus(movie.id.toString(),binding.iconSave)
        }
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .into(binding.imgMoviePoster)
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .into(binding.imgTrailerPoster)

/*
        binding.buttonDownload.setOnClickListener {
            if (videoUrl != null) {
                viewModel.addVideoToFirestore(
                    auth.currentUser!!.uid,
                    movie.title,
                    videoUrl!!,
                    onSuccess = {

                        Toast.makeText(context, "Video added successfullt", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(context, "Video Url doesnt exist", Toast.LENGTH_SHORT).show()
            }
        }
*/
    }

    private fun playTrailer(url: String) {
        val webView: WebView = binding.imgMovieTrailer
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        webView.visibility = View.VISIBLE
    }
}
