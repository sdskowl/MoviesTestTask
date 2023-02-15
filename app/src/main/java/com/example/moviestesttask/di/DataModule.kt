package com.example.moviestesttask.di

import android.content.Context
import androidx.room.Room
import com.example.moviestesttask.data.network.MovieService
import com.example.moviestesttask.data.repository.RepoMovie
import com.example.moviestesttask.data.repository.RepoMovieImpl
import com.example.moviestesttask.data.storage.AppDb
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .client(client)
        .baseUrl("https://api.themoviedb.org/3/")
        .build()

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext context: Context): AppDb = Room.databaseBuilder(
        context,
        AppDb::class.java,
        "movie_db"
    ).build()

    @Provides
    @Singleton
    fun provideRepoMovie(movieService: MovieService, appDb: AppDb): RepoMovie =
        RepoMovieImpl(service = movieService, appDb = appDb)
}