package com.pidh.a5plus.other

object Constants {
    // DB
    const val DB_REMOTE_USERS = "users"
    const val DB_LOCAL = "a5plus.db"

    //API TMDB
    const val TMDB_API_URL = "https://api.themoviedb.org/3/"

    private const val TMDB_URL_IMAGE = "https://image.tmdb.org/t/p"
    const val BASE_URL_POSTER = "$TMDB_URL_IMAGE/w342"
    const val BASE_URL_PROFILE = "$TMDB_URL_IMAGE/w185"
    const val BASE_URL_BACKDROP = "$TMDB_URL_IMAGE/w780"

    const val SIGN_IN_GOOGLE = 1
    const val MOVIE_PUT_EXTRA = "object_movie"
    const val VIDEO_PUT_EXTRA = "key_video"
}