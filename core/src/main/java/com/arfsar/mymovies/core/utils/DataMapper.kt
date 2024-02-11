package com.arfsar.mymovies.core.utils

import com.arfsar.mymovies.core.data.source.local.entity.MoviesEntity
import com.arfsar.mymovies.core.data.source.remote.response.DetailMoviesResponse
import com.arfsar.mymovies.core.data.source.remote.response.MovieResponseItem
import com.arfsar.mymovies.core.domain.model.Movies

object DataMapper {
    fun mapResponseToEntities(input: List<MovieResponseItem>): List<MoviesEntity> {
        val moviesList = ArrayList<MoviesEntity>()
        input.map {
            val movies = MoviesEntity(
                moviesId = it.id,
                title = it.title,
                overview = it.overview,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                poster = it.posterPath,
                backdropPath = it.backdropPath,
                popularity = it.popularity,
                isFavorite = false
            )
            moviesList.add(movies)
        }
        return moviesList
    }

    fun mapEntitiesToDomain(input: List<MoviesEntity>): List<Movies> =
        input.map {
            Movies(
                movieId = it.moviesId,
                title = it.title,
                overview = it.overview,
                poster = it.poster?:"",
                backdrop = it.backdropPath?:"",
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                popularity = it.popularity,
                isFavorite = it.isFavorite
            )
        }


    fun mapDomainToEntity(input: Movies) = MoviesEntity(
        moviesId = input.movieId,
        title = input.title,
        overview = input.overview,
        releaseDate = input.releaseDate,
        voteAverage = input.voteAverage,
        voteCount = input.voteCount,
        poster = input.poster,
        backdropPath = input.backdrop,
        popularity = input.popularity,
        isFavorite = input.isFavorite
    )

    fun mapEntityToDomain(input: MoviesEntity) = Movies(
        movieId = input.moviesId,
        title = input.title,
        overview = input.overview,
        poster = input.poster?:"",
        backdrop = input.backdropPath?:"",
        releaseDate = input.releaseDate,
        voteAverage = input.voteAverage,
        voteCount = input.voteCount,
        popularity = input.popularity,
        isFavorite = input.isFavorite
    )

    fun mapDetailResponseToEntities(input: DetailMoviesResponse): MoviesEntity {
        return MoviesEntity(
            moviesId = input.id,
            title = input.title,
            overview = input.overview,
            releaseDate = input.releaseDate,
            voteAverage = input.voteAverage,
            voteCount = input.voteCount,
            poster = input.posterPath,
            backdropPath = input.backdropPath,
            popularity = input.popularity,
            isFavorite = false
        )
    }

}