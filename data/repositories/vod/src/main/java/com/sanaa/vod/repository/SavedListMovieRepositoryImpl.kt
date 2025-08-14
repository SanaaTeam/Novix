package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import entity.SavedMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import repository.SavedListMovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedListMovieRepositoryImpl @Inject constructor(
    private val localDataSource: LocalSavedMovieDataSource,

    ) : SavedListMovieRepository {

    override suspend fun upsertMovie(movie: SavedMovie) {
        localDataSource.upsertMovie(movie)
    }

    override suspend fun upsertAllMovies(movies: List<SavedMovie>) {
        localDataSource.upsertAllMovies(movies.map { it })
    }

    override suspend fun deleteMovieFromList(movieId: Long, listId: Long) {
        localDataSource.deleteMovieFromList(movieId = movieId, listId = listId)
    }

    override suspend fun deleteAllByListId(listId: Long) {
        localDataSource.deleteAllByListId(listId)
    }

    override suspend fun deleteAllByMovieId(movieId: Long) {
        localDataSource.deleteAllByMovieId(movieId)
    }

    override suspend fun deleteAllMovies() {
        localDataSource.deleteAllSavedMovies()
    }

    override suspend fun getSavedMovieById(movieId: Long): SavedMovie? {
        return localDataSource.getSavedMovieById(movieId)
    }

    override suspend fun getAllSavedMovies(): List<SavedMovie> {
        return localDataSource.getAllSavedMovies().map { it }
    }


    override fun observeSavedMovies(): Flow<List<SavedMovie>> {
        return localDataSource.observeSavedMovies().map { list -> list.map { it } }
    }

    override fun isMovieSaved(movieId: Long, listId: Long): Flow<Boolean> {
        return localDataSource.isMovieSaved(movieId, listId)
    }

    override fun isMovieSaved(movieId: Long): Flow<Boolean> {
        return localDataSource.isMovieSaved(movieId)
    }
}