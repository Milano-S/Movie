package com.mil.movieapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mil.movieapp.model.SavedMovieDetails

@Database(entities = [SavedMovieDetails::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun MovieDao() : MovieDao
}