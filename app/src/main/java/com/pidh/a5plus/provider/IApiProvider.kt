package com.pidh.a5plus.provider

import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.provider.api.response.*

interface IApiProvider {

    suspend fun find(mediaType: String,  id: Long): Movie

    suspend fun searchMovies(query: String): DiscoverResponse

    suspend fun mostPopular(mediaType: String,  genreId: Long): DiscoverResponse

    suspend fun inTheaters(minimumDate: String, maximumDate: String, genreId: Long): DiscoverResponse

    suspend fun castDetail(mediaType: String,  id: Long): CastingResponse

    suspend fun seasons(seriesId: Long): SeasonsResponse

    suspend fun episodes(seriesId: Long, seasonNumber: Int): EpisodesResponse

    suspend fun trailers(mediaType: String,  id: Long): VideosResponse
}