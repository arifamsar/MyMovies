package com.arfsar.mymovies.di

import com.arfsar.mymovies.core.domain.usecase.MoviesInteractor
import com.arfsar.mymovies.core.domain.usecase.MoviesUseCase
import com.arfsar.mymovies.presentation.detail.DetailViewModel
import com.arfsar.mymovies.presentation.home.HomeViewModel
import com.arfsar.mymovies.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MoviesUseCase> { MoviesInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { SettingsViewModel(get()) }

}