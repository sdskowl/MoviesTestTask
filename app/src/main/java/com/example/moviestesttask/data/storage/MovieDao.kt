package com.example.moviestesttask.data.storage

import androidx.paging.PagingSource
import androidx.room.*
import com.example.moviestesttask.data.models.MovieDB

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<MovieDB>)

    @Query(
        "SELECT * FROM movie"
    )
    fun movieHistory(): PagingSource<Int, MovieDB>

    @Query("DELETE FROM movie ")
    suspend fun clearMovies()

}
