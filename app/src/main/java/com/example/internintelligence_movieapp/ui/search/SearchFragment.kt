package com.example.internintelligence_movieapp.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.databinding.FragmentSearchBinding
import com.example.internintelligence_movieapp.ui.home.HomeFragmentDirections
import com.example.internintelligence_movieapp.ui.home.HomeViewModel
import com.example.internintelligence_movieapp.ui.home.adapter.GenresMoviesAdapter


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var genresMoviesAdapter: GenresMoviesAdapter
    val viewModel: SearchViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiKey = "827c2738d945feb56a52ad0fc38dc665"

        genresMoviesAdapter = GenresMoviesAdapter{ movie ->
            movieDetail(movie.title)
        }
        binding.rvGenresMovies.adapter = genresMoviesAdapter
        binding.rvGenresMovies.layoutManager =
            GridLayoutManager(context, 2)
        viewModel.getMovies(apiKey)
        viewModel.movieResult.observe(viewLifecycleOwner) { movieResponse ->
            movieResponse?.results?.let { movies ->

                genresMoviesAdapter.submitList(movies)
            } ?: run {
                Log.e("HomeFragment", "Failed to fetch movies or no data available.")
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchMovies(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun searchMovies(query: String) {
        val apiKey = "827c2738d945feb56a52ad0fc38dc665"
        viewModel.getSearch(apiKey, query)
        viewModel.searchResult.observe(viewLifecycleOwner) { searchResponse ->
            searchResponse?.results?.let { movies ->
                genresMoviesAdapter.submitList(movies)
            } ?: run {
                Log.e("SearchFragment", "No results found for query: $query")
            }
        }
    }

    private fun movieDetail(movieTitle: String?) {
        if (movieTitle != null) {
            val action = SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(movieTitle)
            findNavController().navigate(action)
        } else {
            Log.e("HomeFragment", "Movie title is null")
        }
    }


}