package com.mil.movieapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedMovieDetails(
    @PrimaryKey @ColumnInfo(name = "id") val id : Int,
    @ColumnInfo(name = "name") val name : String = "",
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "posterPath") val posterPath : String,
    @ColumnInfo(name = "isFavorite") var isFavorite : Boolean
)
