package com.pidh.a5plus.provider.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pidh.a5plus.provider.api.entities.Video

data class VideosResponse(@SerializedName("results") @Expose val videos: List<Video>)