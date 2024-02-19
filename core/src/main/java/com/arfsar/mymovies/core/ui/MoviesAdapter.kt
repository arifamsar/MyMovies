package com.arfsar.mymovies.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arfsar.mymovies.core.BuildConfig
import com.arfsar.mymovies.core.databinding.ItemListMoviesBinding
import com.arfsar.mymovies.core.domain.model.Movies
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MoviesAdapter : ListAdapter<Movies, MoviesAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movies)
    }

    inner class ListViewHolder(private val binding: ItemListMoviesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movies) {
            with(binding) {
                val fullImageUrl = "${BuildConfig.imageUrl}${movie.poster}"
                Glide.with(itemView.context)
                    .load(fullImageUrl)
                    .apply(RequestOptions().override(400, 400))
                    .into(ivPoster)
                tvTitle.text = movie.title
                tvDescription.text = movie.overview
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener {
                getItem(position)?.let {
                    onItemClickCallback.onItemClicked(it)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movies>() {
            override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
                return oldItem.movieId == newItem.movieId
            }
            override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
                return oldItem == newItem
            }
        }
    }

}


