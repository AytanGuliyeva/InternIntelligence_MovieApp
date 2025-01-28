package com.example.internintelligence_movieapp.ui.movieDetail

import android.os.Bundle
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
import com.example.internintelligence_movieapp.ui.search.SearchViewModel


class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding
    val args: MovieDetailFragmentArgs by navArgs()
    val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiKey = "827c2738d945feb56a52ad0fc38dc665"

        viewModel.searchMovieByTitle(apiKey, args.title)

        viewModel.movieDetails.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                binding.textTitle.text = movie.title
                binding.textDescription.text = movie.overview
                binding.textDescription2.text = movie.overview
                binding.textYear.text = movie.release_date
                Glide.with(requireContext())
                    .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    .into(binding.imgMoviePoster)
                Glide.with(requireContext())
                    .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    .into(binding.imgTrailerPoster)
            } else {
                binding.textTitle.text = "No movie found"
            }
        }

        viewModel.trailerUrl.observe(viewLifecycleOwner) { trailerUrl ->
            if (trailerUrl != null) {
                binding.buttonPlay.setOnClickListener {
                    val webView: WebView = binding.imgMovieTrailer
                    webView.settings.javaScriptEnabled = true
                    webView.loadUrl(trailerUrl)
                    webView.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireContext(), "No trailer found", Toast.LENGTH_SHORT).show()
            }
        }


        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
