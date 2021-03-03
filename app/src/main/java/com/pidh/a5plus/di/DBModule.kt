package com.pidh.a5plus.di

import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pidh.a5plus.other.Constants.DB_LOCAL
import com.pidh.a5plus.provider.database.local.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    lateinit var database: RoomRepository

    @Provides
    @Singleton
    fun localDatabase(
        @ApplicationContext context: Context
    ): RoomRepository {
        database =  Room.databaseBuilder(
            context,
            RoomRepository::class.java, DB_LOCAL
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    Log.d("Room@addCallback", "Call onCreate")

                    GlobalScope.launch {
                        RoomRepository.onCreate(database)
                    }
                }
            })
            .build()

        //Force created
        database.query("select 1", null)

        return database
    }
}