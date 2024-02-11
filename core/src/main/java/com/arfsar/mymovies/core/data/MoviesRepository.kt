package com.arfsar.mymovies.core.data

import com.arfsar.mymovies.core.data.source.local.LocalDataSource
import com.arfsar.mymovies.core.data.source.local.preferences.SettingsPreferences
import com.arfsar.mymovies.core.data.source.remote.RemoteDataSource
import com.arfsar.mymovies.core.data.source.remote.network.ApiResponse
import com.arfsar.mymovies.core.data.source.remote.response.DetailMoviesResponse
import com.arfsar.mymovies.core.data.source.remote.response.MovieResponseItem
import com.arfsar.mymovies.core.data.source.remote.response.SearchResponse
import com.arfsar.mymovies.core.domain.model.Movies
import com.arfsar.mymovies.core.domain.repository.IMoviesRepository
import com.arfsar.mymovies.core.utils.AppExecutors
import com.arfsar.mymovies.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MoviesRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val settingsPreferences: SettingsPreferences
) : IMoviesRepository {

    override fun getAllMovies(): Flow<Resource<List<Movies>>> =
        object : NetworkBoundResource<List<Movies>, List<MovieResponseItem>>() {
            override fun loadFromDB(): Flow<List<Movies>> {
                return localDataSource.getAllMovies().map { DataMapper.mapEntitiesToDomain(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponseItem>>> =
                remoteDataSource.getPopularMovies()

            override suspend fun saveCallResult(data: List<MovieResponseItem>) {
                val movieList = DataMapper.mapResponseToEntities(data)
                localDataSource.insertMovies(movieList)
            }

            override fun shouldFetch(data: List<Movies>?): Boolean = true

        }.asFlow()

    override fun getFavoriteMovies(): Flow<List<Movies>> {
        return localDataSource.getFavoriteMovies().map { DataMapper.mapEntitiesToDomain(it) }
    }

    override fun setFavoriteMovies(movies: Movies, state: Boolean) {
        val moviesEntity = DataMapper.mapDomainToEntity(movies)
        appExecutors.diskIO().execute { localDataSource.setFavoriteMovies(moviesEntity, state) }
    }

    override fun getDetailMovies(id: Int): Flow<Resource<Movies>> {
        return object : NetworkBoundResource<Movies, DetailMoviesResponse>() {
            override fun loadFromDB(): Flow<Movies> {
                return localDataSource.getMovieById(id).map { DataMapper.mapEntityToDomain(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<DetailMoviesResponse>> {
                return remoteDataSource.getDetailMovies(id)
            }

            override suspend fun saveCallResult(data: DetailMoviesResponse) {
                val movie = DataMapper.mapDetailResponseToEntities(data)
                localDataSource.insertMovies(listOf(movie))
            }

            override fun shouldFetch(data: Movies?): Boolean = data?.movieId == null
        }.asFlow()
    }

    override fun getSearchMovies(query: String): Flow<Resource<List<Movies>>> =
        object : NetworkBoundResource<List<Movies>, List<MovieResponseItem>>() {
            override fun loadFromDB(): Flow<List<Movies>> {
                return localDataSource.getSearchMovies(query).map { DataMapper.mapEntitiesToDomain(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponseItem>>> {
                return remoteDataSource.getSearchMovies(query)
            }

            override suspend fun saveCallResult(data: List<MovieResponseItem>) {
                val movieList = DataMapper.mapResponseToEntities(data)
                localDataSource.insertMovies(movieList)
            }

            override fun shouldFetch(data: List<Movies>?): Boolean = data.isNullOrEmpty()
        }.asFlow()

    override fun getThemeSetting(): Flow<Boolean> = settingsPreferences.getThemeSetting()

    override suspend fun saveThemeSetting(state: Boolean) = settingsPreferences.saveThemeSetting(state)

}