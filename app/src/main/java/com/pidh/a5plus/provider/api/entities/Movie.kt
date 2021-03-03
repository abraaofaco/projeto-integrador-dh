package com.pidh.a5plus.provider.api.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie(
    val id: Long,
     @SerializedName("name")
    private val _name: String,
    val video: Boolean,
    val overview: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("media_type")
    var mediaType: String
) : Serializable {

    @SerializedName("title")
    var title: String = ""
         get() = if (field.isNullOrBlank()) _name else field

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}