package com.arfsar.mymovies.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arfsar.mymovies.R
import com.arfsar.mymovies.core.BuildConfig
import com.arfsar.mymovies.core.data.Resource
import com.arfsar.mymovies.core.domain.model.Movies
import com.arfsar.mymovies.databinding.ActivityDetailBinding
import com.arfsar.mymovies.ui.home.HomeFragment.Companion.EXTRA_DATA
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movieId = intent.getIntExtra(EXTRA_DATA, 0)
        detailViewModel.getDetailMovie(movieId).observe(this) { movie ->
            if (movie != null) {
                when (movie) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        populateMovies(movie.data)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text = movie.message ?: getString(R.string.something_wrong)
                    }
                }
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()

    }


    private fun populateMovies(movies: Movies?) {
        movies?.let {
            supportActionBar?.title = movies.title
            with(binding) {
                tvTitle.text = movies.title
                tvDescription.text = movies.overview
                tvReleaseDate.text = getString(R.string.release_date, movies.releaseDate)
                tvPopularity.text = getString(R.string.popularity, movies.popularity.toString())
                tvRating.text = movies.voteAverage.toString()
                Glide.with(this@DetailActivity)
                    .load(BuildConfig.imageUrl + movies.poster)
                    .into(imgPoster)

                var statusFavorite = movies.isFavorite
                setStatusFavorite(statusFavorite)
                fabFavorite.setOnClickListener {
                    statusFavorite = !statusFavorite
                    detailViewModel.setFavoriteMovie(movies, statusFavorite)
                    setStatusFavorite(statusFavorite)
                }

            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}