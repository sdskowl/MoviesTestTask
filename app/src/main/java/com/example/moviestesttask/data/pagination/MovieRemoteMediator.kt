package com.example.moviestesttask.data.pagination

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.moviestesttask.data.models.Movie
import com.example.moviestesttask.data.models.MovieDB
import com.example.moviestesttask.data.models.RemoteKeys
import com.example.moviestesttask.data.network.MovieService
import com.example.moviestesttask.data.storage.AppDb
import com.haroldadmin.cnradapter.invoke
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private var startPage: Int = 1,
    private val service: MovieService,
    private val appDb: AppDb
) : RemoteMediator<Int, MovieDB>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieDB>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Log.d(TAG, "load type = REFRESH $remoteKeys")
                remoteKeys?.nextKey?.minus(1) ?: startPage
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                Log.d(TAG, "load type = PREPEND prev key = $prevKey")
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                Log.d(TAG, "load type = APPEND, remote keys = $remoteKeys")
                nextKey
            }
        }


        val apiResponse =
            service.getMovies(page = page)
        val giphyData: List<Movie.Result>? = apiResponse.invoke()?.results

        if (giphyData != null) {
            val endOfPaginationReached = giphyData.isEmpty()
            appDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDb.remoteKeysDao().clearRemoteKeys()
                    appDb.movieDao().clearMovies()
                }
                val prevKey = if (page == startPage) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                Log.d(TAG, "prevKey = $prevKey, nextKey = $nextKey page = $page")
                val keys = giphyData.map {
                    RemoteKeys(
                        movieId = it.id.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                val dataDb =
                    giphyData.map {
                       MovieDB(
                           id = it.id.toString(),
                           originalTitle = it.originalTitle,
                           posterPath = it.posterPath,
                           overview = it.overview,
                           voteAverage = it.voteAverage
                       )
                    }
                appDb.remoteKeysDao().insertAll(keys)
                appDb.movieDao().insertAll(dataDb)
            }



            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } else return MediatorResult.Error(fail("Something wrong"))

    }

    private fun fail(message: String): Throwable {
        return IOException(message)
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieDB>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
            appDb.withTransaction { appDb.remoteKeysDao().remoteKeysMovieId(movie.id) }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieDB>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                // Get the remote keys of the first items retrieved
                appDb.withTransaction { appDb.remoteKeysDao().remoteKeysMovieId(movie.id) }
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieDB>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { movie ->
                appDb.withTransaction { appDb.remoteKeysDao().remoteKeysMovieId(movie.id) }
            }
        }
    }
    
    companion object {
        private const val TAG = "MediatorRemote"
    }
}