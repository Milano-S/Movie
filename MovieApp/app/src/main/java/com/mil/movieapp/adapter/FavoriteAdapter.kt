package com.mil.movieapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mil.movie.urls.Urls
import com.mil.movieapp.R
import com.mil.movieapp.model.SavedMovieDetails

class FavoriteAdapter(
    val context: Context,
    val savedMovieList : List<SavedMovieDetails>
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var deleteListener : OnMovieDelete

    interface OnMovieDelete{
        fun onMovieDelete(position: Int)
    }
    fun onMovieDeleteListener(listener: OnMovieDelete){
        deleteListener = listener
    }

    inner class ViewHolder(itemView : View, listener: OnMovieDelete): RecyclerView.ViewHolder(itemView){

        init {
            val ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
            ivDelete.setOnClickListener {
                listener.onMovieDelete(layoutPosition)
            }
        }

        fun bind(movieDetails: SavedMovieDetails){
            val tvName = itemView.findViewById<TextView>(R.id.tvMovieName)
            val ivPoster = itemView.findViewById<ImageView>(R.id.imageView)
            val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)

            val posterUri = Uri.parse(Urls.posterUrl + movieDetails.posterPath)

            tvName.text = movieDetails.name
            Glide.with(context).load(posterUri).into(ivPoster)
            tvDescription.text = movieDetails.overview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_card_favorite, parent, false)
        return ViewHolder(view, deleteListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedMovieList[position])
    }

    override fun getItemCount() = savedMovieList.size

}