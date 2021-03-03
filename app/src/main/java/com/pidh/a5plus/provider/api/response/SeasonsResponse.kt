package com.pidh.a5plus.provider.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pidh.a5plus.provider.api.entities.SeriesInfo

class SeasonsResponse(@SerializedName("seasons") @Expose val listSeasons: List<SeriesInfo>)