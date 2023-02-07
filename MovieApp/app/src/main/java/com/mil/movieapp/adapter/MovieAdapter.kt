package com.mil.movieapp.adapter

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mil.movieapp.model.Result
import com.mil.movie.urls.Urls
import com.mil.movieapp.R

class MovieAdapter(
    val context: Context,
    val movieList: List<Result>
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var favoriteListener : OnMovieSave

    interface OnMovieSave {
        fun onMovieSave(position: Int)
    }

    fun onMovieSaveListener(listener : OnMovieSave){
        favoriteListener = listener
    }

    inner class ViewHolder(
        itemView: View,
        ivFavorite: ImageView,
        listener: OnMovieSave,
    ) : RecyclerView.ViewHolder(itemView) {

        val tvName = itemView.findViewById<TextView>(R.id.tvMovieName)
        val ivPoster = itemView.findViewById<ImageView>(R.id.imageView)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        val ivFavoriteA = itemView.findViewById<ImageView>(R.id.ivFavorite)

        init {
            ivFavoriteA.setOnClickListener {
                ivFavoriteA.setBackgroundResource(R.drawable.star_solid)
                listener.onMovieSave(layoutPosition)
            }
        }

        fun bind(movieDetails: Result) {

            val posterUri = Uri.parse(Urls.posterUrl + movieDetails.poster_path)

            tvName.text = movieDetails.original_title
            Glide.with(context).load(posterUri).into(ivPoster)
            tvDescription.text = movieDetails.overview
            if (movieDetails.isFavorite){
               ivFavoriteA.setBackgroundResource(R.drawable.star_solid)
            }else{
                ivFavoriteA.setBackgroundResource(R.drawable.star_outline)
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

        holder.ivFavoriteA.setBackgroundResource(R.drawable.star_outline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false)
        return ViewHolder(view,view.findViewById(R.id.imageView), favoriteListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount() = movieList.size

    /*private fun toggleButton(toggleButton: ToggleButton,listener: OnMovieSave, position: Int){
        toggleButton.isChecked = false
        toggleButton.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.star_solid
            )
        )
        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) toggleButton.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.star_outline
                )
            ) else toggleButton.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.star_solid
                )
            )
        }
        //listener.onMovieSave(position)
    }*/

}