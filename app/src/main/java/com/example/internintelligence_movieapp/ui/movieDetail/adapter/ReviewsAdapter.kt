package com.example.internintelligence_movieapp.ui.movieDetail.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.databinding.ItemGenresMoviesBinding
import com.example.internintelligence_movieapp.databinding.ItemReviewBinding
import com.example.internintelligence_movieapp.retrofit.model.Movie
import com.example.internintelligence_movieapp.retrofit.model.Review

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallBack)

    fun submitList(reviewList: List<Review>) {
        differ.submitList(reviewList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.textAuthor.text = "⭐ ${review.author}:"
            binding.textReview.text = review.content

        }
    }
}
