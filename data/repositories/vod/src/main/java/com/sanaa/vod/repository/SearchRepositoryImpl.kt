package com.sanaa.vod.repository

import android.util.Log
import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.search.SearchRemoteDataSource
import com.sanaa.vod.mapper.search.toEntity
import com.sanaa.vod.mapper.search.toLocalDto
import com.sanaa.vod.util.filterCachedMovies
import com.sanaa.vod.util.filterCachedTvShows
import com.sanaa.vod.util.filterMovies
import com.sanaa.vod.util.filterTvShows
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.SearchRepository
import repository.SearchRepository.SearchResult
import usecase.search.search_param.MediaFilters

class SearchRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localCacheSearchDataSource: LocalCacheSearchDataSource,
    private val languageProvider: LanguageProvider,
) : SearchRepository {
    override suspend fun searchActors(query: String, page: Int): SearchResult<Actor> =
        safeCall(query) {
            val pageSize = 20
            val offset = (page - 1) * pageSize

            val cachedActorsWithTotalPagesNumber =
                localCacheSearchDataSource.getPagedActorsByQuery(query, pageSize, offset).also { Log.d("TAG", "searchTvShows: $it") }
            if (cachedActorsWithTotalPagesNumber.second.isNotEmpty()) {
                val actors = cachedActorsWithTotalPagesNumber.second.map { it.toEntity() }
                return SearchResult(
                    totalPages = cachedActorsWithTotalPagesNumber.first,
                    results = actors
                )
            } else {
                val response = remoteDataSource.searchActors(query, page)
                    val actors = response.results.onEach {
                    val language = languageProvider.getCurrentLanguage()
                    localCacheSearchDataSource.cacheActor(it.toLocalDto(language))
                }.map { it.toEntity() }

                return SearchResult(
                    totalPages = response.totalPages,
                    results = actors
                )
            }
        }

    override suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): SearchResult<Movie> = safeCall(query) {
        val pageSize = 20
        val offset = (page - 1) * pageSize

        val cachedMedia = localCacheSearchDataSource.getMoviesByQuery(
            query, limit = pageSize, offset = offset
        ).also { Log.d("TAG", "searchTvShows: $it") }

        return if (cachedMedia.second.isNotEmpty()) {
            SearchResult(
                totalPages = cachedMedia.first,
                results = getMoviesFromCache(filters, cachedMedia.second)
            )
        } else {
            getMoviesFromRemote(query, page, filters)
        }

    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): SearchResult<TvSeries> = safeCall(query) {
        val pageSize = 20
        val offset = (page - 1) * pageSize

        val cachedTvSeries = localCacheSearchDataSource.getTvSeriesByQuery(
            query, limit = pageSize, offset = offset
        ).also { Log.d("TAG", "searchTvShows: $it") }
        return if (cachedTvSeries.second.isNotEmpty()){
            SearchResult(
                totalPages = cachedTvSeries.first,
                results = getTvSeriesFromCache(filters, cachedTvSeries.second)
            )
        }
        else
            getTvSeriesFromRemote(query, page, filters)
    }

    private suspend fun getMoviesFromRemote(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): SearchResult<Movie> {
        val movies = remoteDataSource.searchMovies(query, page).results.onEach {
            localCacheSearchDataSource.cacheMovie(it.toLocalDto(languageProvider.getCurrentLanguage()))
        }
        val filteredMovies = filters?.filterMovies(movies)

        return SearchResult(
            totalPages = remoteDataSource.searchMovies(query, page).totalPages,
            results = filteredMovies ?: movies.map { it.toEntity() }
        )
    }

    private fun getMoviesFromCache(
        filters: MediaFilters?,
        cachedMedia: List<MovieLocalDto>,
    ): List<Movie> {
        return filters
            ?.filterCachedMovies(cachedMedia)
            ?: cachedMedia.map { it.toEntity() }
    }

    private suspend fun getTvSeriesFromRemote(
        query: String,
        page: Int,
        filters: MediaFilters?,
    ): SearchResult<TvSeries> {
        val tvSeries = remoteDataSource.searchTvShows(query, page).results.onEach {
            localCacheSearchDataSource.cacheTvSeries(
                it.toLocalDto(languageProvider.getCurrentLanguage())
            )
        }
        val filteredTvSeries = filters
            ?.filterTvShows(tvSeries = tvSeries)
            ?: tvSeries.map { it.toEntity() }
        return SearchResult(
            totalPages = remoteDataSource.searchMovies(query, page).totalPages,
            results = filteredTvSeries
        )
    }

    private fun getTvSeriesFromCache(
        filters: MediaFilters?,
        cachedTvSeries: List<TvSeriesLocalDto>,
    ): List<TvSeries> {
        return filters
            ?.filterCachedTvShows(cachedTvSeries)
            ?: return cachedTvSeries.map { it.toEntity() }
    }
}