package com.pidh.a5plus.provider.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pidh.a5plus.provider.database.local.dao.GenreDao
import com.pidh.a5plus.provider.database.local.entities.Genre

@Database(entities = [Genre::class], version = 1, exportSchema = false)
abstract class RoomRepository : RoomDatabase() {
    abstract fun genreDao(): GenreDao

    companion object {
        suspend fun onCreate(db: RoomRepository) {
            val movie = arrayOf(
                Genre(codeMovie = 28, mediaType = "movie", name = "Ação"),
                Genre(codeMovie = 10749, mediaType = "movie", name = "Romance"),
                Genre(codeMovie = 12, mediaType = "movie", name = "Aventura"),
                Genre(codeMovie = 35, mediaType = "movie", name = "Comédia"),
                Genre(codeMovie = 16, mediaType = "movie", name = "Animação"),
                Genre(codeMovie = 878, mediaType = "movie", name = "Ficção científica"),
                Genre(codeMovie = 10751, mediaType = "movie", name = "Família"),
                Genre(codeMovie = 37, mediaType = "movie", name = "Faroeste"),
                Genre(codeMovie = 27, mediaType = "movie", name = "Terror"),
            )

            val tv = arrayOf(
                Genre(codeMovie = 10759, mediaType = "tv", name = "Ação e aventura"),
                Genre(codeMovie = 10749, mediaType = "tv", name = "Romance"),
                Genre(codeMovie = 18, mediaType = "tv", name = "Drama"),
                Genre(codeMovie = 35, mediaType = "tv", name = "Comédia"),
                Genre(codeMovie = 10765, mediaType = "tv", name = "Ficção científica"),
                Genre(codeMovie = 10751, mediaType = "tv", name = "Família"),
                Genre(codeMovie = 10762, mediaType = "tv", name = "Kids"),
                Genre(codeMovie = 16, mediaType = "tv", name = "Animação"),
                Genre(codeMovie = 9648, mediaType = "tv", name = "Mistério"),
            )

            val genreDao = db.genreDao()

            genreDao.insertAll(*movie)
            genreDao.insertAll(*tv)
        }
    }
}