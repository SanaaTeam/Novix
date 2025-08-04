package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.remote.SearchRemoteDataSource
import com.sanaa.vod.repository.mapper.history.toEntity
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
) : SearchRepository {

    override suspend fun searchActors(query: String, page: Int): List<Actor> = safeCall(query) {
        remoteDataSource.searchActors(query, page).results.map { it.toEntity() }
    }

    override suspend fun searchMovies(
        query: String,
        page: Int,
    ): List<Movie> = safeCall(query) {
        remoteDataSource.searchMovies(query, page).results.map { it.toEntity() }
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
    ): List<TvSeries> = safeCall(query) {
        return remoteDataSource.searchTvShows(query, page).results.map { it.toEntity() }
    }
}