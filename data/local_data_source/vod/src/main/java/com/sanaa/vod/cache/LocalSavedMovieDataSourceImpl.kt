package com.sanaa.vod.cache

import com.sanaa.vod.cache.dao.SavedListMovieDao
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.repository.mapper.custom_list.toDomain
import com.sanaa.vod.repository.mapper.custom_list.toDto
import entity.SavedMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalSavedMovieDataSourceImpl @Inject constructor(
    private val dao: SavedListMovieDao,
) : LocalSavedMovieDataSource {

    override suspend fun upsertMovie(movie: SavedMovie) {
        dao.upsertMovie(movie.toDto())
    }

    override suspend fun upsertAllMovies(movies: List<SavedMovie>) {
        dao.upsertAllMovies(movies.map { it.toDto() })
    }

    override suspend fun deleteMovieFromList(movieId: Long, listId: Long) {
        dao.deleteMovieFromList(listId = listId, movieId = movieId)
    }

    override suspend fun deleteAllByMovieId(movieId: Long) {
        dao.deleteAllByMovieId(movieId)
    }

    override suspend fun deleteAllByListId(listId: Long) {
        dao.deleteAllByListId(listId)
    }

    override suspend fun deleteAllSavedMovies() {
        dao.deleteAllSavedMovies()
    }

    override suspend fun getSavedMovieById(movieId: Long): SavedMovie? {
        return dao.getSavedMovieById(movieId)?.toDomain()
    }

    override suspend fun getAllSavedMovies(): List<SavedMovie> {
        return dao.getSavedMovies().map { it.toDomain() }
    }

    override fun observeSavedMovies(): Flow<List<SavedMovie>> {
        return dao.observeSavedMovies().map { list -> list.map { it.toDomain() } }
    }

    override fun isMovieSaved(movieId: Long, listId: Long): Flow<Boolean> {
        return dao.isMovieSavedFlow(movieId, listId)
    }

    override fun isMovieSaved(movieId: Long): Flow<Boolean> {
        return dao.isMovieSavedFlow(movieId)
    }
}