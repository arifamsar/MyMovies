package com.arfsar.mymovies.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arfsar.mymovies.core.domain.usecase.MoviesUseCase

class FavoriteViewModel(moviesUseCase: MoviesUseCase) : ViewModel() {
    val favoriteMovies = moviesUseCase.getFavoriteMovies().asLiveData()
}