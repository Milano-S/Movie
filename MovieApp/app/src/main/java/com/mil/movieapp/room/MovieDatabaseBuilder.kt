package com.mil.movieapp.room

import android.content.Context
import androidx.room.Room

object MovieDatabaseBuilder {

    private var INSTANCE: MovieDatabase? = null

    fun getInstance(context: Context): MovieDatabase{
        if (INSTANCE == null) {
            synchronized(MovieDatabase::class) {
                INSTANCE = buildMovieDb(context)
            }
        }
        return INSTANCE!!
    }
    private fun buildMovieDb(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        MovieDatabase::class.java,
        "movie-data"
    ).build()
}