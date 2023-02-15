package com.example.moviestesttask.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviestesttask.data.models.MovieDB
import com.example.moviestesttask.data.models.RemoteKeys

@Database(
    entities = [MovieDB::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}