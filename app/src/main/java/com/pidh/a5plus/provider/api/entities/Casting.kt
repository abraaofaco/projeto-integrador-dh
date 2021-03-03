package com.pidh.a5plus.provider.api.entities

import com.google.gson.annotations.SerializedName

class Casting(
    val id: Long,
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String,
    val order: Int
)