package com.sanaa.vod.repository

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.search.SearchRemoteDataSource
import com.sanaa.vod.mapper.search.toEntity
import com.sanaa.vod.mapper.search.toLocalDto
import com.sanaa.vod.util.filterCashedMovies
import com.sanaa.vod.util.filterCashedTvShows
import com.sanaa.vod.util.filterMovies
import com.sanaa.vod.util.filterTvShows
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.SearchRepository
import usecase.search.search_param.MediaFilters

class SearchRepositoryImpl(
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
        filters: MediaFilters?,
    ): List<Movie> = safeCall(query) {
        val pageSize = 20
        val offset = (page - 1) * pageSize

        val cachedMedia = localCacheSearchDataSource.getMoviesByQuery(
            query, limit = pageSize, offset = offset
        )
        if (cachedMedia.isNotEmpty())
            getMoviesFromCache(filters, cachedMedia)
        else
            getMoviesFromRemote(query, page, filters)
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<TvSeries> = safeCall(query) {
        val pageSize = 20
        val offset = (page - 1) * pageSize

        val cachedTvSeries = localCacheSearchDataSource.getTvSeriesByQuery(
            query, limit = pageSize, offset = offset
        )
        if (cachedTvSeries.isNotEmpty())
            getTvSeriesFromCache(filters, cachedTvSeries)
        else
            getTvSeriesFromRemote(query, page, filters)
    }

    private suspend fun getMoviesFromRemote(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<Movie> {
        val movies = remoteDataSource.searchMovies(query, page).results.onEach {
            localCacheSearchDataSource.cacheMovie(it.toLocalDto(languageProvider.getCurrentLanguage()))
        }
        return filters
            ?.filterMovies(movies)
            ?: movies.map { it.toEntity() }
    }

    private fun getMoviesFromCache(
        filters: MediaFilters?,
        cachedMedia: List<MovieLocalDto>,
    ): List<Movie> {
        return filters
            ?.filterCashedMovies(cachedMedia)
            ?: cachedMedia.map { it.toEntity() }
    }

    private suspend fun getTvSeriesFromRemote(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<TvSeries> {
        val tvSeries = remoteDataSource.searchTvShows(query, page).results.onEach {
            localCacheSearchDataSource.cacheTvSeries(
                it.toLocalDto(languageProvider.getCurrentLanguage())
            )
        }
        return filters
            ?.filterTvShows(tvSeries = tvSeries)
            ?: return tvSeries.map { it.toEntity() }
    }

    private fun getTvSeriesFromCache(
        filters: MediaFilters?,
        cachedTvSeries: List<TvSeriesLocalDto>,
    ): List<TvSeries> {
        return filters
            ?.filterCashedTvShows(cachedTvSeries)
            ?: return cachedTvSeries.map { it.toEntity() }
    }
}