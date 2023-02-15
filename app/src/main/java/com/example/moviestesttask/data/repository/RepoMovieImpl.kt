package com.example.moviestesttask.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviestesttask.data.models.MovieDB
import com.example.moviestesttask.data.network.MovieService
import com.example.moviestesttask.data.pagination.MovieRemoteMediator
import com.example.moviestesttask.data.storage.AppDb
import kotlinx.coroutines.flow.Flow

class RepoMovieImpl(
    private val service: MovieService,
    private val appDb: AppDb
) : RepoMovie {
    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<MovieDB>> {
        val pagingSourceFactory = { appDb.movieDao().movieHistory() }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = true),
            remoteMediator = MovieRemoteMediator(service = service, appDb = appDb),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}