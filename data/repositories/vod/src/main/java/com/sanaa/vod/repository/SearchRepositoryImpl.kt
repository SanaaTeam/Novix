package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.remote.SearchRemoteDataSource
import com.sanaa.vod.repository.mapper.history.toEntity
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.SavedListsStatusProvider
import repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
    private val savedListsStatusProvider: SavedListsStatusProvider
) : SearchRepository {

    override suspend fun searchActors(query: String, page: Int): List<Actor> = safeCall(query) {
        remoteDataSource.searchActors(query, page).results.map { it.toEntity() }
    }

    override suspend fun searchMovies(query: String, page: Int): List<Movie> = safeCall(query) {
        remoteDataSource.searchMovies(query, page).results.map { dto ->
            val movie = dto.toEntity()
            movie.copy(isSaved = savedListsStatusProvider.isItemSaved(movie.id))
        }
    }

    override suspend fun searchTvShows(query: String, page: Int): List<TvSeries> = safeCall(query) {
        remoteDataSource.searchTvShows(query, page).results.map { it.toEntity() }
    }
}