package com.mil.movie.urls

class Urls {

    companion object{

        const val appKey = "2d6a452100bd9331663fd97e616f2a26"

        const val baseUrl = "https://api.themoviedb.org/"

        const val trendingDayUrl = "/3/trending/all/day?api_key=$appKey"

        const val trendingWeekUrl = "/3/trending/all/week?api_key=$appKey"

        const val posterUrl = "https://image.tmdb.org/t/p/original/"
    }

}