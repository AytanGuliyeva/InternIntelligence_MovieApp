package com.example.internintelligence_movieapp.ui.saved

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.base.Resource
import com.example.internintelligence_movieapp.databinding.FragmentSavedBinding
import com.example.internintelligence_movieapp.ui.home.adapter.GenresMoviesAdapter
import com.example.internintelligence_movieapp.ui.movieDetail.MovieDetailViewModel
import com.example.internintelligence_movieapp.ui.saved.adapter.SavedAdapter
import com.example.internintelligence_movieapp.ui.search.SearchFragmentDirections

class SavedFragment : Fragment() {
    private lateinit var binding: FragmentSavedBinding
    private lateinit var savedAdapter: SavedAdapter
    val viewModel: SavedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedAdapter = SavedAdapter { movie ->
            movieDetail(movie.title)
        }
        binding.rvSaves.adapter = savedAdapter
        binding.rvSaves.layoutManager = GridLayoutManager(context, 2)

        viewModel.fetchSavedMovies()
        Log.e("TAG", "onViewCreated: ${viewModel.fetchSavedMovies()}", )

        viewModel.movieResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Yüklənir göstərgəsini aktivləşdirin
                }
                is Resource.Success -> {
                    resource.data?.let { movies ->
                        savedAdapter.submitList(movies)
                        Log.e("TAG", "Adapter list: $movies")
                    }
                }
                is Resource.Error -> {
                    Log.e("SavedFragment", "Error: ${resource.exception?.message}")
                }
            }
        }


    }

    private fun movieDetail(movieTitle: String?) {
        if (movieTitle != null) {
            val action = SavedFragmentDirections.actionSavedFragmentToMovieDetailFragment(movieTitle)
            findNavController().navigate(action)
        } else {
            Log.e("SavedFragment", "Movie title is null")
        }
    }
}
