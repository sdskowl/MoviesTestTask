package com.example.moviestesttask.data.repository

import androidx.paging.PagingData
import com.example.moviestesttask.data.models.MovieDB
import kotlinx.coroutines.flow.Flow

interface RepoMovie {

    fun getMovies(): Flow<PagingData<MovieDB>>

}