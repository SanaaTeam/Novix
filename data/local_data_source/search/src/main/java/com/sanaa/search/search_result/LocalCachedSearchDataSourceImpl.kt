package com.sanaa.search.search_result

import com.example.env_config.service.LanguageProvider
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.local.dto.ActorLocalDto
import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.dataSource.local.dto.SearchLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.search_result.dao.ActorDao
import com.sanaa.search.search_result.dao.MovieDao
import com.sanaa.search.search_result.dao.SearchDao
import com.sanaa.search.search_result.dao.SearchResultDao
import com.sanaa.search.search_result.dao.SeriesDao

class LocalCachedSearchDataSourceImpl(
    private val searchDao: SearchDao,
    private val searchResultDao: SearchResultDao,
    private val actorDao: ActorDao,
    private val movieDao: MovieDao,
    private val seriesDao: SeriesDao,
    private val languageProvider: LanguageProvider
) : LocalCacheSearchDataSource {

    private val currentLanguage: String
        get() = languageProvider.getCurrentLanguage()

    override suspend fun cacheSearchResult(query: String, itemId: Int, itemType: String) {
        val existingSearch = searchDao.getSearchByQueryAndLanguage(query, currentLanguage)

        val searchId = if (existingSearch != null) {
            searchDao.updateTimestamp(query, currentLanguage, System.currentTimeMillis())
            existingSearch.id
        } else {
            searchDao.insertSearch(
                SearchLocalDto(
                    query = query,
                    language = currentLanguage
                )
            )
        }

        clearExpiredCache(System.currentTimeMillis() - CACHE_EXPIRATION_TIME)
        searchResultDao.insert(
            SearchResultLocalDto(
                id = searchId.toInt(),
                itemId = itemId,
                itemType = itemType
            )
        )
    }

    override suspend fun getCachedResults(query: String, type: String): List<SearchResultLocalDto> {
        clearExpiredCache(System.currentTimeMillis() - CACHE_EXPIRATION_TIME)

        val search = searchDao.getSearchByQueryAndLanguage(query, currentLanguage)

        if (search == null || isExpired(search.timestamp)) {
            return emptyList()
        }

        searchDao.updateTimestamp(query, currentLanguage, System.currentTimeMillis())

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


    override suspend fun getActorsByQuery(query: String): List<ActorLocalDto> {
        val cachedResults = getCachedResults(query, "actor")

        if (cachedResults.isNotEmpty()) {
            return cachedResults.mapNotNull { result ->
                actorDao.getActorsByQuery(result.itemId.toString()).firstOrNull()
            }
        }

        return actorDao.getActorsByQuery(query)
    }

    override suspend fun getMoviesByQuery(query: String): List<MovieLocalDto> {
        val cachedResults = getCachedResults(query, "movie")

        if (cachedResults.isNotEmpty()) {
            return cachedResults.mapNotNull { result ->
                movieDao.getFilteredMovies(query = result.itemId.toString()).firstOrNull()
            }
        }

        return movieDao.getFilteredMovies(query = query)
    }

    override suspend fun getTvSeriesByQuery(query: String): List<TvSeriesLocalDto> {
        val cachedResults = getCachedResults(query, "tv_series")

        if (cachedResults.isNotEmpty()) {
            return cachedResults.mapNotNull { result ->
                seriesDao.getFilteredSeries(result.itemId.toString()).firstOrNull()
            }
        }

        return emptyList()
    }

    override suspend fun clearExpiredCache(expirationTime: Long) {
        searchResultDao.deleteOldResults(expirationTime)
    }

    fun isExpired(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp > CACHE_EXPIRATION_TIME
    }

    companion object {
        const val CACHE_EXPIRATION_TIME = 60 * 60 * 1000L
    }
}