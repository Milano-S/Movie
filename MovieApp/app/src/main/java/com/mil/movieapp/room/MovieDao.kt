package com.mil.movieapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mil.movieapp.model.SavedMovieDetails

@Dao
interface MovieDao {

    @Query("SELECT * FROM SavedMovieDetails")
    suspend fun getAllMovies(): List<SavedMovieDetails>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movieDetails: SavedMovieDetails)

    @Query("DELETE FROM SavedMovieDetails WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Int)

    @Query("DELETE FROM SavedMovieDetails")
    suspend fun deleteAllMovies()
}