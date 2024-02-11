package com.arfsar.mymovies.core.domain.repository

import com.arfsar.mymovies.core.data.Resource
import com.arfsar.mymovies.core.domain.model.Movies
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

    fun getAllMovies(): Flow<Resource<List<Movies>>>

    fun getFavoriteMovies(): Flow<List<Movies>>

    fun setFavoriteMovies(movies: Movies, state: Boolean)

    fun getDetailMovies(id: Int): Flow<Resource<Movies>>

    fun getSearchMovies(query: String): Flow<Resource<List<Movies>>>

    fun getThemeSetting(): Flow<Boolean>

    suspend fun saveThemeSetting(state: Boolean)
}