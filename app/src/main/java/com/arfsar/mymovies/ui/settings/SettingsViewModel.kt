package com.arfsar.mymovies.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.arfsar.mymovies.core.domain.usecase.MoviesUseCase
import kotlinx.coroutines.launch

class SettingsViewModel(private val moviesUseCase: MoviesUseCase) : ViewModel() {
    fun getThemeSetting() = moviesUseCase.getThemeSetting().asLiveData()
    fun saveThemeSetting(state: Boolean) {
        viewModelScope.launch {
            moviesUseCase.saveThemeSetting(state)
        }
    }
}