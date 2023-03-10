package com.mil.movieapp.model

import androidx.room.Entity
import com.mil.movieapp.model.Result

data class MovieDetails(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)