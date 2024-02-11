package com.arfsar.mymovies.core.data.source.local

import com.arfsar.mymovies.core.data.source.local.entity.MoviesEntity
import com.arfsar.mymovies.core.data.source.local.preferences.SettingsPreferences
import com.arfsar.mymovies.core.data.source.local.room.MoviesDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val moviesDao: MoviesDao) {

    fun getAllMovies(): Flow<List<MoviesEntity>> = moviesDao.getAllMovies()

    fun getFavoriteMovies(): Flow<List<MoviesEntity>> = moviesDao.getFavoriteMovies()

    suspend fun insertMovies(moviesList: List<MoviesEntity>) = moviesDao.insertMovies(moviesList)

    fun setFavoriteMovies(movies: MoviesEntity, newState: Boolean) {
        movies.isFavorite = newState
        moviesDao.updateFavoriteMovies(movies)
    }

    fun getMovieById(movieId: Int): Flow<MoviesEntity> = moviesDao.getDetailMovies(movieId)

    fun getSearchMovies(query: String): Flow<List<MoviesEntity>> = moviesDao.getSearchMovies(query)


}