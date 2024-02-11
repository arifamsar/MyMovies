package com.arfsar.mymovies.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RecentResponse(

	@field:SerializedName("dates")
	val dates: Dates,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("results")
	val results: List<MovieResponseItem>,

	@field:SerializedName("total_results")
	val totalResults: Int
)


data class Dates(

	@field:SerializedName("maximum")
	val maximum: String,

	@field:SerializedName("minimum")
	val minimum: String
)
