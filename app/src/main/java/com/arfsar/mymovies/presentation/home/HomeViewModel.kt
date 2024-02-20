package com.arfsar.mymovies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.arfsar.mymovies.core.domain.usecase.MoviesUseCase
import kotlinx.coroutines.launch

class HomeViewModel(private val moviesUseCase: MoviesUseCase) : ViewModel() {

    fun getMovies() = moviesUseCase.getAllMovies().asLiveData()

    fun searchMovies(query: String) = moviesUseCase.getSearchMovies(query).asLiveData()

    fun getThemeSetting() = moviesUseCase.getThemeSetting().asLiveData()

    fun saveThemeSetting(state: Boolean) {
        viewModelScope.launch {
            moviesUseCase.saveThemeSetting(state)
        }
    }
}