package com.pidh.a5plus.provider.api.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SeriesInfo(
    val id: Long,
    val name: String,
    val overview: String,
    @SerializedName("episode_count")
    val episodeCount: Int,
    @SerializedName("episode_number")
    val episodeNumber: Int,
    @SerializedName("still_path")
    private val _stillPath: String,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("air_date")
    val airDate: String
) : Serializable {
    @SerializedName("poster_path")
    val posterPath: String = ""
        get() = if (field.isNullOrBlank()) _stillPath else field

}
