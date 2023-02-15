package com.example.moviestesttask.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.moviestesttask.data.models.MovieDB
import com.example.moviestesttask.data.repository.RepoMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
     repoMovie: RepoMovie
): ViewModel() {

    val pagingDataFlow: Flow<PagingData<MovieDB>> = repoMovie.getMovies()
}