package com.sanaa.search.data.local.datasource

import com.sanaa.search.data.local.dao.SearchDao
import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.data.local.dao.ActorDao
import com.sanaa.search.data.local.dao.MovieDao
import com.sanaa.search.data.local.dao.SeriesDao
import com.sanaa.search.dataSource.local.dto.SearchLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import repository.LanguageProvider

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
                id = searchId,
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

    override suspend fun cacheActor(actorsLocalDto: ActorsLocalDto) {
        actorDao.insertActor(actorsLocalDto)
    }

    override suspend fun cacheMovie(moviesLocalDto: MoviesLocalDto) {
        movieDao.insertMovie(moviesLocalDto)
    }

    override suspend fun cacheTvSeries(tvSeriesLocalDto: TvSeriesLocalDto) {
        seriesDao.insertSeries(tvSeriesLocalDto)
    }

    override suspend fun getActorsByQuery(query: String): List<ActorsLocalDto> {
        val cachedResults = getCachedResults(query, "actor")
        
        if (cachedResults.isNotEmpty()) {
            return cachedResults.mapNotNull { result ->
                actorDao.getActorByQuery(result.itemId.toString())
            }
        }
        
        return listOfNotNull(actorDao.getActorByQuery(query))
    }

    override suspend fun getMoviesByQuery(query: String): List<MoviesLocalDto> {
        val cachedResults = getCachedResults(query, "movie")
        
        if (cachedResults.isNotEmpty()) {
            // Return cached movies based on cached results
            return cachedResults.mapNotNull { result ->
                movieDao.getFilteredMovies(query = result.itemId.toString()).firstOrNull()
            }
        }
        
        return movieDao.getFilteredMovies(query = query)
    }

    override suspend fun getTvSeriesByQuery(query: String): List<TvSeriesLocalDto> {
        val cachedResults = getCachedResults(query, "tv_series")
        
        if (cachedResults.isNotEmpty()) {
            // Return cached TV series based on cached results
            return cachedResults.mapNotNull { result ->
                seriesDao.getFilteredSeries(
                    minYear = 1900,
                    maxYear = 2030,
                    genre = "",
                    rating = 0f
                ).firstOrNull()
            }
        }

        return emptyList()
    }

    override suspend fun clearExpiredCache(expirationTime: Long) {
        searchResultDao.deleteOldResults(expirationTime)
    }

    private fun isExpired(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp > CACHE_EXPIRATION_TIME
    }

    companion object {
        private const val CACHE_EXPIRATION_TIME = 60 * 60 * 1000L // 1 hour
        private const val TAG = "LocalCachedSearchDataSource"
    }
} 