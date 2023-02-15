package com.example.moviestesttask.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieDB(
    @PrimaryKey val id: String,
    val originalTitle: String,
    val posterPath: String,
    val overview: String,
    val voteAverage: Double
)