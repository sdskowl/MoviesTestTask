package com.example.moviestesttask.data.network

import com.example.moviestesttask.data.models.Movie
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/top_rated")
    suspend fun getMovies(
        @Query("api_key", encoded = true) api: String = API_KEY,
        @Query("page", encoded = true) page: Int = 1
    ): NetworkResponse<Movie, String>

    companion object {
        private const val API_KEY = "d866f943f2d70dee6fcd311d094d5720"
    }
}