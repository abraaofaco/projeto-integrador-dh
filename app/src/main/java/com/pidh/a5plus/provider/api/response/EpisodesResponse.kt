package com.pidh.a5plus.provider.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pidh.a5plus.provider.api.entities.SeriesInfo

class EpisodesResponse(
    val name: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("episodes")
    @Expose
    val listEpisodes: List<SeriesInfo>
)