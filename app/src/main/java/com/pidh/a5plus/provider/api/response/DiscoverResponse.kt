package com.pidh.a5plus.provider.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pidh.a5plus.provider.api.entities.Movie

data class DiscoverResponse(
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results") @Expose val movies: Set<Movie>
)