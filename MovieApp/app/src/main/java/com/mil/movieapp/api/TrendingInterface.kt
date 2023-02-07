package com.mil.movie.api

import com.mil.movieapp.model.MovieDetails
import com.mil.movie.urls.Urls
import retrofit2.Call
import retrofit2.http.GET

interface TrendingInterface {

    @GET(Urls.trendingWeekUrl)
    fun getTrendingMovies() : Call<MovieDetails>

}