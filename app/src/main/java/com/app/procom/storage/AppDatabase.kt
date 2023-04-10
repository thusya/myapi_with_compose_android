package com.app.procom.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.procom.storage.movies.MovieDetails
import com.app.procom.storage.movies.MoviesDao
import com.app.procom.util.Constants.DATABASE_NAME

@Database(entities = [MovieDetails::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null)
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .build()
                }
            return INSTANCE as AppDatabase
        }
    }
}