package com.sanaa.vod.repository

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.search.SearchRemoteDataSource
import com.sanaa.vod.mapper.search.toLocalDto
import com.sanaa.vod.mapper.search.toSearchOutput
import com.sanaa.vod.util.filterCashedMovies
import com.sanaa.vod.util.filterCashedTvShows
import com.sanaa.vod.util.filterMovies
import com.sanaa.vod.util.filterTvShows
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import repository.SearchRepository
import usecase.search.search_param.MediaFilters
import usecase.search.search_param.SearchMovieOutput
import usecase.search.search_param.SearchTvSeriesOutput
import java.nio.channels.UnresolvedAddressException

class SearchRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localCacheSearchDataSource: LocalCacheSearchDataSource,
    private val languageProvider: LanguageProvider,
) : SearchRepository {
    override suspend fun searchActors(query: String, page: Int) = searchOrThrow(query) {
        val pageSize = 20
        val offset = (page - 1) * pageSize

        val cachedActors = localCacheSearchDataSource.getPagedActorsByQuery(query, pageSize, offset)
        if (cachedActors.isNotEmpty()) {
            cachedActors.map { it.toSearchOutput() }
        } else {
            remoteDataSource.searchActors(query, page).results.onEach {
                val language = languageProvider.getCurrentLanguage()
                localCacheSearchDataSource.cacheActor(it.toLocalDto(language))
            }.map { it.toSearchOutput() }
        }
    }

    override suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<SearchMovieOutput> = searchOrThrow(query) {
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
    ): List<SearchTvSeriesOutput> = searchOrThrow(query) {
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
    ): List<SearchMovieOutput> {
        val movies = remoteDataSource.searchMovies(query, page).results.onEach {
            localCacheSearchDataSource.cacheMovie(it.toLocalDto(languageProvider.getCurrentLanguage()))
        }
        return filters
            ?.filterMovies(movies)
            ?: movies.map { it.toSearchOutput() }
    }

    private fun getMoviesFromCache(
        filters: MediaFilters?,
        cachedMedia: List<MovieLocalDto>,
    ): List<SearchMovieOutput> {
        return filters
            ?.filterCashedMovies(cachedMedia)
            ?: cachedMedia.map { it.toSearchOutput() }
    }

    private suspend fun getTvSeriesFromRemote(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): List<SearchTvSeriesOutput> {
        val tvSeries = remoteDataSource.searchTvShows(query, page).results.onEach {
            localCacheSearchDataSource.cacheTvSeries(
                it.toLocalDto(languageProvider.getCurrentLanguage())
            )
        }
        return filters
            ?.filterTvShows(tvSeries = tvSeries)
            ?: return tvSeries.map { it.toSearchOutput() }
    }

    private fun getTvSeriesFromCache(
        filters: MediaFilters?,
        cachedTvSeries: List<TvSeriesLocalDto>,
    ): List<SearchTvSeriesOutput> {
        return filters
            ?.filterCashedTvShows(cachedTvSeries)
            ?: return cachedTvSeries.map { it.toSearchOutput() }
    }

    private suspend fun <T> searchOrThrow(
        query: String,
        callee: suspend () -> T,
    ): T {
        try {
            return callee()
        } catch (_: UnresolvedAddressException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RetrievingDataFailureException("Failed to retrieve data for query: $query")
        }
    }
}