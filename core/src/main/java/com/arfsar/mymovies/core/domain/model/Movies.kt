package com.arfsar.mymovies.core.domain.model

import android.os.Parcelable
import com.arfsar.mymovies.core.data.source.remote.response.GenresItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movies(
    val movieId: Int,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val isFavorite: Boolean
): Parcelable