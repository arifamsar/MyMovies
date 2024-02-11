package com.arfsar.mymovies.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arfsar.mymovies.core.domain.model.Movies
import com.arfsar.mymovies.core.domain.usecase.MoviesUseCase

class DetailViewModel(private val moviesUseCase: MoviesUseCase): ViewModel() {
    fun setFavoriteMovie(movie: Movies, newStatus: Boolean) {
        moviesUseCase.setFavoriteMovies(movie, newStatus)
    }

    fun getDetailMovie(id: Int) = moviesUseCase.getDetailMovies(id).asLiveData()

}