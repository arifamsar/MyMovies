package com.arfsar.mymovies.core.data.source.remote.network

import com.arfsar.mymovies.core.data.source.remote.response.DetailMoviesResponse
import com.arfsar.mymovies.core.data.source.remote.response.PopularResponse
import com.arfsar.mymovies.core.data.source.remote.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(): PopularResponse

    @GET("movie/{movie_id}")
    suspend fun getDetailMovies(
        @Path("movie_id") movieId: Int
    ): DetailMoviesResponse

    @GET("discover/movie")
    suspend fun getSearchMovies(
        @Query("with_text_query") query: String
    ): SearchResponse
}