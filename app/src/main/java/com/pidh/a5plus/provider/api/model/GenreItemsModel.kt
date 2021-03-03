package com.pidh.a5plus.provider.api.model

import com.pidh.a5plus.provider.api.entities.Movie

data class GenreItemsModel(val genreId: Long, val genreName: String, val items: Set<Movie>)