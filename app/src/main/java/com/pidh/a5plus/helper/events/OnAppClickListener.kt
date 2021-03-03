package com.pidh.a5plus.helper.events

import com.pidh.a5plus.provider.api.entities.Movie
import com.pidh.a5plus.provider.api.entities.SeriesInfo
import com.pidh.a5plus.provider.api.entities.Video
import com.pidh.a5plus.provider.maps.entities.PlaceInfo

interface OnAppClickListener {
    fun onItemClick(movie: Movie) {

    }

    fun onItemClick(season: SeriesInfo) {

    }

    fun onItemClick(place: PlaceInfo) {

    }

    fun onItemClick(video: Video) {

    }
}