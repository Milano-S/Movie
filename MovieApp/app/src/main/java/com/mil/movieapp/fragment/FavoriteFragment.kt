package com.mil.movieapp.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mil.movieapp.R
import com.mil.movieapp.adapter.FavoriteAdapter
import com.mil.movieapp.databinding.FragmentFavoriteBinding
import com.mil.movieapp.model.SavedMovieDetails
import com.mil.movieapp.room.MovieDao
import com.mil.movieapp.room.MovieDatabaseBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


private const val TAG = "FavoriteFragment"

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var movieListFavorite: List<SavedMovieDetails>
    private lateinit var adapter: FavoriteAdapter
    private val db: MovieDao by lazy {
        MovieDatabaseBuilder.getInstance(requireContext()).MovieDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val pa = (activity as AppCompatActivity)
        val actionBar = pa.supportActionBar
        actionBar?.apply {
            title = "Watch Later"
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)

        movieListFavorite = runBlocking { db.getAllMovies() }
        initializeAdapter(requireContext())
    }

    private fun initializeAdapter(context: Context) {
        adapter = FavoriteAdapter(context, movieListFavorite)
        binding.rvFav.adapter = adapter
        binding.rvFav.layoutManager = LinearLayoutManager(context)
        adapter.onMovieDeleteListener(object : FavoriteAdapter.OnMovieDelete {
            override fun onMovieDelete(position: Int) {
                val currentMovie = movieListFavorite[position]
                (movieListFavorite as MutableList).removeAt(position)
                CoroutineScope(Dispatchers.IO).launch { db.deleteMovie(currentMovie.id) }
                refreshFragment()
            }
        })
    }

    private fun refreshFragment(){
        val navController = findNavController()
        navController.run {
            popBackStack()
            navigate(R.id.favoriteFragment)
        }
    }
}