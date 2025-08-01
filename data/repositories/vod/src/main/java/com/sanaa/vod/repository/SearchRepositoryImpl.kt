package com.sanaa.vod.repository

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.search.SearchRemoteDataSource
import com.sanaa.vod.mapper.search.toEntity
import com.sanaa.vod.mapper.search.toLocalDto
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localCacheSearchDataSource: LocalCacheSearchDataSource,
    private val languageProvider: LanguageProvider,
) : SearchRepository {
    override suspend fun searchActors(query: String, page: Int): List<Actor> = safeCall(query) {
        val pageSize = 20
        val offset = (page - 1) * pageSize

        val cachedActors = localCacheSearchDataSource.getPagedActorsByQuery(query, pageSize, offset)
        if (cachedActors.isNotEmpty()) {
            cachedActors.map { it.toEntity() }
        } else {
            remoteDataSource.searchActors(query, page).results.onEach {
                val language = languageProvider.getCurrentLanguage()
                localCacheSearchDataSource.cacheActor(it.toLocalDto(language))
            }.map { it.toEntity() }
        }
    }

    override suspend fun searchMovies(
        query: String,
        page: Int,
    ): List<Movie> = safeCall(query) {
        val pageSize = 20
        val offset = (page - 1) * pageSize

        val cachedMedia = localCacheSearchDataSource.getMoviesByQuery(
            query, limit = pageSize, offset = offset
        )
        if (cachedMedia.isNotEmpty())
            getMoviesFromCache(cachedMedia)
        else
            getMoviesFromRemote(query, page)
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
    ): List<TvSeries> = safeCall(query) {
        val pageSize = 20
        val offset = (page - 1) * pageSize

        val cachedTvSeries = localCacheSearchDataSource.getTvSeriesByQuery(
            query, limit = pageSize, offset = offset
        )
        if (cachedTvSeries.isNotEmpty())
            getTvSeriesFromCache(cachedTvSeries)
        else
            getTvSeriesFromRemote(query, page)
    }

    private suspend fun getMoviesFromRemote(
        query: String,
        page: Int,
    ): List<Movie> {
        return remoteDataSource.searchMovies(query, page).results.onEach {
            localCacheSearchDataSource.cacheMovie(it.toLocalDto(languageProvider.getCurrentLanguage()))
        }.map { it.toEntity() }
    }

    private fun getMoviesFromCache(
        cachedMedia: List<MovieLocalDto>,
    ): List<Movie> {
        return cachedMedia.map { it.toEntity() }
    }

    private suspend fun getTvSeriesFromRemote(
        query: String,
        page: Int,
    ): List<TvSeries> {
        return remoteDataSource.searchTvShows(query, page).results.onEach {
            localCacheSearchDataSource.cacheTvSeries(
                it.toLocalDto(languageProvider.getCurrentLanguage())
            )
        }.map { it.toEntity() }
    }

    private fun getTvSeriesFromCache(
        cachedTvSeries: List<TvSeriesLocalDto>,
    ): List<TvSeries> {
        return cachedTvSeries.map { it.toEntity() }
    }
}