package com.example.internintelligence_movieapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.databinding.FragmentHomeBinding
import com.example.internintelligence_movieapp.ui.home.adapter.GenresMoviesAdapter
import com.example.internintelligence_movieapp.ui.home.adapter.MoviesImageAdapter
import com.example.internintelligence_movieapp.ui.home.adapter.PopularMoviesAdapter
import com.example.internintelligence_movieapp.ui.home.adapter.TopRatedMoviesAdapter
import com.example.internintelligence_movieapp.ui.me.MeViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels()
    private lateinit var popularAdapter: PopularMoviesAdapter
    private lateinit var topRatedAdapter: TopRatedMoviesAdapter
    private lateinit var genresMoviesAdapter: GenresMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiKey = "827c2738d945feb56a52ad0fc38dc665"
        setupRecyclerViewAdapters()
        fetchMovies(apiKey)



        viewModel.popularMovieResult.observe(viewLifecycleOwner) { movieResponse ->
            movieResponse?.results?.let { movies ->

                popularAdapter.submitList(movies)
            } ?: run {
                Log.e("HomeFragment", "Failed to fetch movies or no data available.")
            }
        }
        viewModel.topRatedMovieResult.observe(viewLifecycleOwner) { movieResponse ->
            movieResponse?.results?.let { movies ->

                topRatedAdapter.submitList(movies)
            } ?: run {
                Log.e("HomeFragment", "Failed to fetch movies or no data available.")
            }
        }
        viewModel.nowPlayingMoviesResult.observe(viewLifecycleOwner) { movieResponse ->
            movieResponse?.results?.let { movies ->
                val limitedMovies = movies.take(5)
                val adapter = MoviesImageAdapter(
                    limitedMovies.mapNotNull { it.poster_path }
                ) {}
                binding.viewPager.adapter = adapter
                val dotsIndicator = binding.dotsIndicator
                dotsIndicator.setViewPager2(binding.viewPager)
            } ?: run {
                Log.e("HomeFragment", "Failed to fetch movies or no data available.")
            }
        }
        viewModel.movieGenres.observe(viewLifecycleOwner) { genres ->
            genres?.let {
                val chipGroup = binding.chipGroup
                chipGroup.removeAllViews()

                genres.genres?.forEach { genre ->
                    val chip = Chip(requireContext())
                    chip.text = genre.name
                    chip.isCheckable = true
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            viewModel.getGenreMovies(apiKey, genre.id)
                        } else {
                            viewModel.getPopularMovies(apiKey)
                        }
                    }
                    chipGroup.addView(chip)
                }
            }
        }


        viewModel.movieGenresMovies.observe(viewLifecycleOwner) { genres ->
            Log.e("TAG", "onViewCreated: $genres")
        }

        viewModel.movieGenresMovies.observe(viewLifecycleOwner) { movieResponse ->
            movieResponse?.results?.let { movies ->
                genresMoviesAdapter.submitList(movies)
            } ?: run {
                Log.e("HomeFragment", "Failed to fetch genre movies or no data available.")
            }
        }


    }

    private fun setupRecyclerViewAdapters() {
        popularAdapter = PopularMoviesAdapter { movie -> movieDetail(movie.title) }
        binding.rvPopular.adapter = popularAdapter
        binding.rvPopular.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        topRatedAdapter = TopRatedMoviesAdapter { movie -> movieDetail(movie.title) }
        binding.rvLatest.adapter = topRatedAdapter
        binding.rvLatest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        genresMoviesAdapter = GenresMoviesAdapter { movie -> movieDetail(movie.title) }
        binding.rvGenresMovies.adapter = genresMoviesAdapter
        binding.rvGenresMovies.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }


    private fun fetchMovies(apiKey: String) {
        viewModel.getPopularMovies(apiKey)
        viewModel.getTopRatedMovies(apiKey)
        viewModel.getNowPlayingMovies(apiKey)
        viewModel.getGenres(apiKey)
    }

    private fun movieDetail(movieTitle: String?) {
        if (movieTitle != null) {
            val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(movieTitle)
            findNavController().navigate(action)
        } else {
            Log.e("HomeFragment", "Movie title is null")
        }
    }

}



