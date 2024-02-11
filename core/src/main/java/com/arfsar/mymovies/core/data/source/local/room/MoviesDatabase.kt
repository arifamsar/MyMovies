package com.arfsar.mymovies.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arfsar.mymovies.core.data.source.local.entity.MoviesEntity

@Database(entities = [MoviesEntity::class], version = 2, exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

}