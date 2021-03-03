package com.pidh.a5plus.provider.api.tmdb

import com.pidh.a5plus.provider.IApiProvider
import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.provider.api.response.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBRepository : IApiProvider {

    @GET("{mediaType}/{id}")
    override suspend fun find(
        @Path("mediaType") mediaType: String,
        @Path("id") id: Long
    ): Movie

    @GET("search/multi")
    override suspend fun searchMovies(
        @Query("query") query: String
    ): DiscoverResponse

    @GET("discover/{mediaType}?sort_by=popularity.desc")
    override suspend fun mostPopular(
        @Path("mediaType") mediaType: String,
        @Query("with_genres") genreId: Long,
    ) : DiscoverResponse

    @GET("discover/movie?with_release_type=2|3")
    override suspend fun inTheaters(
        @Query("primary_release_date.gte") minimumDate: String,
        @Query("primary_release_date.lte") maximumDate: String,
        @Query("with_genres") genreId: Long
    ) : DiscoverResponse

    @GET("{mediaType}/{id}/credits")
    override suspend fun castDetail(
        @Path("mediaType") mediaType: String,
        @Path("id") id: Long
    ): CastingResponse

    @GET("tv/{id}")
    override suspend fun seasons(
        @Path("id") seriesId: Long
    ): SeasonsResponse

    @GET("tv/{id}/season/{season}")
    override suspend fun episodes(
        @Path("id") seriesId: Long,
        @Path("season") seasonNumber: Int
    ): EpisodesResponse

    @GET("{mediaType}/{id}/videos")
    override suspend fun trailers(
        @Path("mediaType") mediaType: String,
        @Path("id") id: Long
    ): VideosResponse
}