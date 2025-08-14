package com.sanaa.vod.dataSource.local.cache


import entity.SavedMovie
import kotlinx.coroutines.flow.Flow

interface LocalSavedMovieDataSource {

    suspend fun upsertMovie(movie: SavedMovie)
    suspend fun upsertAllMovies(movies: List<SavedMovie>)

    suspend fun deleteMovieFromList(movieId: Long, listId: Long)
    suspend fun deleteAllByListId(listId: Long)
    suspend fun deleteAllByMovieId(movieId: Long)
    suspend fun deleteAllSavedMovies()

    suspend fun getSavedMovieById(movieId: Long): SavedMovie?
    suspend fun getAllSavedMovies(): List<SavedMovie>

    fun observeSavedMovies(): Flow<List<SavedMovie>>
    fun isMovieSaved(movieId: Long, listId: Long): Flow<Boolean>
    fun isMovieSaved(movieId: Long): Flow<Boolean>
}