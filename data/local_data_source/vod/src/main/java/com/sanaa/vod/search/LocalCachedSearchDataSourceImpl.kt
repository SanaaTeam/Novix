package com.sanaa.vod.search

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.dataSource.local.search.dto.ActorLocalDto
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.SearchLocalDto
import com.sanaa.vod.dataSource.local.search.dto.SearchResultLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.search.dao.ActorDao
import com.sanaa.vod.search.dao.MovieDao
import com.sanaa.vod.search.dao.SearchDao
import com.sanaa.vod.search.dao.SearchResultDao
import com.sanaa.vod.search.dao.SeriesDao
import com.sanaa.vod.util.TimeUtils
import javax.inject.Inject

class LocalCachedSearchDataSourceImpl @Inject constructor(
    private val searchDao: SearchDao,
    private val searchResultDao: SearchResultDao,
    private val actorDao: ActorDao,
    private val movieDao: MovieDao,
    private val seriesDao: SeriesDao,
    private val languageProvider: LanguageProvider,
) : LocalCacheSearchDataSource {

    private val currentLanguage: String
        get() = languageProvider.getCurrentLanguage()

    override suspend fun cacheSearchResult(query: String, itemId: Int, itemType: String) {
        val existingSearch = searchDao.getSearchByQueryAndLanguage(query, currentLanguage)
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()

        val searchId = if (existingSearch != null) {
            searchDao.updateTimestamp(query, currentLanguage, currentTimestamp)
            existingSearch.id
        } else {
            searchDao.insertSearch(
                SearchLocalDto(
                    query = query,
                    language = currentLanguage
                )
            )
        }

        clearExpiredCache(currentTimestamp - CACHE_EXPIRATION_TIME)
        searchResultDao.insert(
            SearchResultLocalDto(
                id = searchId.toInt(),
                itemId = itemId,
                itemType = itemType
            )
        )
    }

    override suspend fun getCachedResults(query: String, type: String): List<SearchResultLocalDto> {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        clearExpiredCache(currentTimestamp - CACHE_EXPIRATION_TIME)

        val search = searchDao.getSearchByQueryAndLanguage(query, currentLanguage)

        if (search == null || isExpired(search.timestamp)) {
            return emptyList()
        }

        searchDao.updateTimestamp(query, currentLanguage, currentTimestamp)

        return searchResultDao.getByQueryAndLanguage(query, currentLanguage, type)
    }

    override suspend fun cacheActor(actorLocalDto: ActorLocalDto) {
        actorDao.insertActor(actorLocalDto)
    }

    override suspend fun cacheMovie(movieLocalDto: MovieLocalDto) {
        movieDao.insertMovie(movieLocalDto)
    }

    override suspend fun cacheTvSeries(tvSeriesLocalDto: TvSeriesLocalDto) {
        seriesDao.insertSeries(tvSeriesLocalDto)
    }

    override suspend fun getPagedActorsByQuery(
        query: String,
        limit: Int,
        offset: Int,
    ): List<ActorLocalDto> {
        return actorDao.getPagedActorsByQuery(query, limit, offset)
    }

    override suspend fun getActorsByQuery(query: String): List<ActorLocalDto> {
        val cachedResults = getCachedResults(query, "actor")

        if (cachedResults.isNotEmpty()) {
            return cachedResults.mapNotNull { result ->
                actorDao.getActorsByQuery(result.itemId.toString()).firstOrNull()
            }
        }

        return emptyList()
    }

    override suspend fun getMoviesByQuery(
        query: String,
        limit: Int,
        offset: Int,
    ): List<MovieLocalDto> {
        val cachedResults = getCachedResults(query, "movie")

        if (cachedResults.isNotEmpty()) {
            val moviesFromIds = cachedResults.mapNotNull { result ->
                movieDao.getMovieById(result.itemId)
            }
            if (moviesFromIds.isNotEmpty()) {
                return moviesFromIds
            }
        }

        return movieDao.getFilteredMovies(query = query, limit = limit, offset = offset)
    }


    override suspend fun getTvSeriesByQuery(
        query: String,
        limit: Int,
        offset: Int,
    ): List<TvSeriesLocalDto> {
        val cachedResults = getCachedResults(query, "tv_series")

        if (cachedResults.isNotEmpty()) {
            return cachedResults.mapNotNull { result ->
                seriesDao.getFilteredSeries(query = result.itemId.toString(), limit, offset)
                    .firstOrNull()
            }
        }
        return seriesDao.getFilteredSeries(query = query, limit = limit, offset = offset)
    }

    override suspend fun clearExpiredCache(expirationTime: Long) {
        searchResultDao.deleteOldResults(expirationTime)
    }

    private fun isExpired(timestamp: Long): Boolean {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        return currentTimestamp - timestamp > CACHE_EXPIRATION_TIME
    }

    companion object {
        const val CACHE_EXPIRATION_TIME = 60 * 60 * 1000L
    }
}