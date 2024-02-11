package com.arfsar.mymovies.core.domain.usecase

import com.arfsar.mymovies.core.data.Resource
import com.arfsar.mymovies.core.domain.model.Movies
import com.arfsar.mymovies.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class MoviesInteractor(private val moviesRepository: IMoviesRepository) : MoviesUseCase {
    override fun getAllMovies(): Flow<Resource<List<Movies>>> = moviesRepository.getAllMovies()

    override fun getFavoriteMovies(): Flow<List<Movies>> = moviesRepository.getFavoriteMovies()

    override fun setFavoriteMovies(movies: Movies, state: Boolean) =
        moviesRepository.setFavoriteMovies(movies, state)

    override fun getDetailMovies(id: Int): Flow<Resource<Movies>> =
        moviesRepository.getDetailMovies(id)

    override fun getSearchMovies(query: String): Flow<Resource<List<Movies>>> =
        moviesRepository.getSearchMovies(query)

    override fun getThemeSetting(): Flow<Boolean> = moviesRepository.getThemeSetting()

    override suspend fun saveThemeSetting(state: Boolean) = moviesRepository.saveThemeSetting(state)
}