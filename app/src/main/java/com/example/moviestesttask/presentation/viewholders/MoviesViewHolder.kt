package com.example.moviestesttask.presentation.viewholders

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.moviestesttask.data.models.MovieDB
import com.example.moviestesttask.databinding.MovieItemBinding

class MoviesViewHolder(
    private val binding: MovieItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    private var currentMovieDB: MovieDB? = null

    fun bind(movie: MovieDB?) {
        currentMovieDB = movie

        movie?.let { m ->
            with(binding) {
               // id.text = m.id
                title.text = m.originalTitle
                vote.text = m.voteAverage.toString()
                overview.text = m.overview
                loadImage(m.posterPath)
            }
        }
    }
    private fun loadImage(path: String) {
        val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        with(binding) {
                Glide.with(poster)
                    .load("https://image.tmdb.org/t/p/w500$path")
                    .placeholder(circularProgressDrawable)
                    .error(circularProgressDrawable)
                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                    .centerCrop()
                    .fitCenter()
                    .into(poster)
        }
    }
    companion object {
        fun create(
            binding: MovieItemBinding
        ): MoviesViewHolder {
            return MoviesViewHolder(binding)
        }
    }
}