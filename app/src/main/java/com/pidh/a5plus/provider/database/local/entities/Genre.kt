package com.pidh.a5plus.provider.database.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genre(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val codeMovie: Long,
    val mediaType: String,
    val name: String,
    var enable: Boolean = true
)