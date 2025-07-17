package com.sanaa.search.repository

import com.example.env_config.service.LanguageProvider
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.mapper.toLocalDto
import com.sanaa.search.mapper.toSearchOutput
import com.sanaa.search.repository.util.filterCashedMovies
import com.sanaa.search.repository.util.filterCashedTvShows
import com.sanaa.search.repository.util.filterMovies
import com.sanaa.search.repository.util.filterTvShows
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import search.repository.SearchRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchMovieOutput
import search.usecase.search_param.SearchTvSeriesOutput
import java.nio.channels.UnresolvedAddressException

class SearchRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localDataSource: LocalCacheSearchDataSource,
    private val languageProvider: LanguageProvider,
) : SearchRepository {
    override suspend fun searchActors(query: String) = searchOrThrow(query) {
        val cachedActors = localDataSource.getActorsByQuery(query)
        if (cachedActors.isNotEmpty()) {
            cachedActors.map { it.toSearchOutput() }
        } else {
            remoteDataSource.searchActors(query).results.onEach {
                val language = languageProvider.getCurrentLanguage()
                localDataSource.cacheActor(it.toLocalDto(language))
            }.map { it.toSearchOutput() }
        }
    }

    override suspend fun searchMovies(
        query: String,
        filters: MediaFilters?,
    ): List<SearchMovieOutput> = searchOrThrow(query) {
        val cachedMedia = localDataSource.getMoviesByQuery(query)
        if (cachedMedia.isNotEmpty())
            getMoviesFromCache(filters, cachedMedia)
        else
            getMoviesFromRemote(query, filters)
    }

    override suspend fun searchTvShows(
        query: String,
        filters: MediaFilters?,
    ): List<SearchTvSeriesOutput> = searchOrThrow(query) {
        val cachedTvSeries = localDataSource.getTvSeriesByQuery(query)
        if (cachedTvSeries.isNotEmpty())
            getTvSeriesFromCache(filters, cachedTvSeries)
        else
            getTvSeriesFromRemote(query, filters)
    }

    private suspend fun getMoviesFromRemote(
        query: String,
        filters: MediaFilters?,
    ): List<SearchMovieOutput> {
        val movies = remoteDataSource.searchMovies(query).results.onEach {
            localDataSource.cacheMovie(it.toLocalDto(languageProvider.getCurrentLanguage()))
        }
        return filters
            ?.filterMovies(movies)
            ?: movies.map { it.toSearchOutput() }
    }

    private fun getMoviesFromCache(
        filters: MediaFilters?,
        cachedMedia: List<MoviesLocalDto>,
    ): List<SearchMovieOutput> {
        return filters
            ?.filterCashedMovies(cachedMedia)
            ?: cachedMedia.map { it.toSearchOutput() }
    }

    private suspend fun getTvSeriesFromRemote(
        query: String,
        filters: MediaFilters?,
    ): List<SearchTvSeriesOutput> {
        val tvSeries = remoteDataSource.searchTvShows(query).results.onEach {
            localDataSource.cacheTvSeries(
                it.toLocalDto(languageProvider.getCurrentLanguage())
            )
        }
        return filters
            ?.filterTvShows(tvSeries)
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

    private suspend fun <T> searchOrThrow(query: String, callee: suspend () -> T): T {
        try {
            return callee()
        } catch (_: UnresolvedAddressException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve data for query: $query")
        }
    }
}