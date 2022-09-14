package com.app.musicapp.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.musicapp.entity.MusicEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [MusicEntity::class]
)
abstract class DatabaseImpl : RoomDatabase() {

    abstract fun musicDao(): MusicDao

    companion object {

        @Volatile
        private var INSTANCE: DatabaseImpl? = null

        private const val database = "database-music.db"

        fun getInstance(context: Context): DatabaseImpl {
            return if (INSTANCE != null) { INSTANCE!! } else {
                synchronized(lock = this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseImpl::class.java,
                        database
                    ).build()

                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
}