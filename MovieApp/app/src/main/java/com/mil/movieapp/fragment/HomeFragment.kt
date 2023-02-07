package com.mil.movieapp.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mil.movie.api.TrendingInterface
import com.mil.movie.urls.Urls
import com.mil.movieapp.adapter.MovieAdapter
import com.mil.movieapp.databinding.FragmentHomeBinding
import com.mil.movieapp.model.MovieDetails
import com.mil.movieapp.model.Result
import com.mil.movieapp.model.SavedMovieDetails
import com.mil.movieapp.room.MovieDao
import com.mil.movieapp.room.MovieDatabaseBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapter: MovieAdapter

    private lateinit var movieDetails: List<Result>

    private val db: MovieDao by lazy { MovieDatabaseBuilder.getInstance(requireContext()).MovieDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        hideActionBar()
        movieDetails = mutableListOf()

        // Inflate the layout for this fragment
        return inflater.inflate(com.mil.movieapp.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        getTrendingMovies()
        initializeAdapter(requireContext(), movieDetails)

    }

    private fun getTrendingMovies() {

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Urls.baseUrl)
            .build()
            .create(TrendingInterface::class.java)

        val retrofitData = retrofitBuilder.getTrendingMovies()

        retrofitData.enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.body() != null) {
                    val responseBody = response.body()!!
                    response.body()!!.results.forEach { movie ->
                        (movieDetails as MutableList<Result>).add(
                            movie
                        )
                    }
                    initializeAdapter(requireContext(), movieDetails)
                    log(responseBody.results.toString())
                }
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                log(t.message.toString())
            }
        })
    }

    private fun saveMovie(movie: Result) {
        CoroutineScope(Dispatchers.IO).launch {
            db.insertMovie(
                SavedMovieDetails(
                    id = movie.id,
                    name = movie.original_title,
                    overview = movie.overview,
                    posterPath = movie.poster_path,
                    isFavorite = movie.isFavorite
                )
            )
        }
    }

    private fun initializeAdapter(context: Context, movieList: List<Result>) {
        val savedMovieList = runBlocking { db.getAllMovies() }
        val savedMovieListIds = mutableListOf<Int>()
        val movieListIds = mutableListOf<Int>()
        savedMovieList.forEach{ movie ->
            savedMovieListIds.add(movie.id)
        }
        movieList.forEach { movie ->
            movieListIds.add(movie.id)
        }
        (movieList as MutableList).forEach { movie ->
            if (movie.id in savedMovieListIds){
                movie.isFavorite = true
            }
        }
        adapter = MovieAdapter(context, movieList)
        binding.rvHome.adapter = adapter
        binding.rvHome.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter.onMovieSaveListener(object : MovieAdapter.OnMovieSave {
            override fun onMovieSave(position: Int) {
                val currentMovie = movieList[position]
                currentMovie.isFavorite = true
                saveMovie(currentMovie)
            }
        })
    }

    private fun hideActionBar() {
        val pa = (activity as AppCompatActivity)
        val actionBar = pa.supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Trending"
            //hide()
        }
    }

    private fun log(message: String) {
        Log.i(TAG, message)
    }
}