package com.arfsar.mymovies.core.data.source.remote

import android.util.Log
import com.arfsar.mymovies.core.data.source.remote.network.ApiResponse
import com.arfsar.mymovies.core.data.source.remote.network.ApiService
import com.arfsar.mymovies.core.data.source.remote.response.DetailMoviesResponse
import com.arfsar.mymovies.core.data.source.remote.response.MovieResponseItem
import com.arfsar.mymovies.core.data.source.remote.response.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getPopularMovies(): Flow<ApiResponse<List<MovieResponseItem>>> {
        return flow {
            try {
                val response = apiService.getPopularMovies()
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNowPlayingMovies(): Flow<ApiResponse<List<MovieResponseItem>>> {
        return flow {
            try {
                val response = apiService.getNowPlayingMovies()
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailMovies(movieId: Int): Flow<ApiResponse<DetailMoviesResponse>> {
        return flow {
            try {
                val response = apiService.getDetailMovies(movieId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchMovies(query: String): Flow<ApiResponse<List<MovieResponseItem>>> {
        return flow {
            try {
                val response = apiService.getSearchMovies(query)
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

}