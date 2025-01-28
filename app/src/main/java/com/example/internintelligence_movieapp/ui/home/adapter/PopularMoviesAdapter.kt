package com.example.internintelligence_movieapp.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internintelligence_movieapp.databinding.ItemPopularMoviesBinding
import com.example.internintelligence_movieapp.retrofit.model.Movie
import com.example.internintelligence_movieapp.retrofit.model.MovieResponse

class PopularMoviesAdapter(var itemClick: (item: Movie) -> Unit) :
    RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder>() {

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }


    }

    private val differ = AsyncListDiffer(this, diffUtilCallBack)

    fun submitList(movieList: List<Movie>) {
        differ.submitList(movieList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        val binding = ItemPopularMoviesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopularMoviesViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class PopularMoviesViewHolder(private val binding: ItemPopularMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movies: Movie) {
            val baseUrl = "https://image.tmdb.org/t/p/w500"
            Glide.with(binding.imgMoviePoster.context)
                .load("$baseUrl${movies.poster_path}")
                .into(binding.imgMoviePoster)

            itemView.setOnClickListener {
                Log.d("Adapter", "Item clicked: ${movies.title}")
                itemClick(movies)
            }
        }

    }
}