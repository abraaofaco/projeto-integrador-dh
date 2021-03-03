package com.pidh.a5plus.provider.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pidh.a5plus.provider.database.local.entities.Genre

@Dao
interface GenreDao {
    @Query("SELECT * FROM genre WHERE mediaType LIKE :mediaType")
    fun get(mediaType: String): List<Genre>

    @Insert
    fun insertAll(vararg genres: Genre)

    @Update
    suspend fun update(genre: Genre)
}