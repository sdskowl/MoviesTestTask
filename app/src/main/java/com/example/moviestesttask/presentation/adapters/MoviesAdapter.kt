package com.example.moviestesttask.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.moviestesttask.data.models.MovieDB
import com.example.moviestesttask.databinding.MovieItemBinding
import com.example.moviestesttask.presentation.viewholders.MoviesViewHolder

class MoviesAdapter(
) :
    PagingDataAdapter<MovieDB, MoviesViewHolder>(MOVIE_COMPARATOR) {

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = getItem(position)
        movie.let { m ->
            holder.bind(m)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: MovieItemBinding = MovieItemBinding.inflate(inflater, parent, false)
        return MoviesViewHolder.create(binding)
    }

    object MOVIE_COMPARATOR : DiffUtil.ItemCallback<MovieDB>() {
        override fun areItemsTheSame(oldItem: MovieDB, newItem: MovieDB): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDB, newItem: MovieDB): Boolean {
            return oldItem == newItem
        }

    }

}