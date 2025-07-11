package com.sanaa.search.data.local.datasource

import com.sanaa.search.data.local.dao.SearchDao
import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.data.local.dao.ActorDao
import com.sanaa.search.data.local.dao.MovieDao
import com.sanaa.search.data.local.dao.SeriesDao
import com.sanaa.search.dataSource.local.dto.SearchLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.LocalCachedSearchDataSource
import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto

class LocalCachedSearchDataSourceImpl(
    private val searchDao: SearchDao,
    private val searchResultDao: SearchResultDao,
    private val actorDao: ActorDao,
    private val movieDao: MovieDao,
    private val seriesDao: SeriesDao
) : LocalCachedSearchDataSource {

 
   
    override suspend fun addSearchResult(
        query: String,
        language: String,
        itemId: Int,
        itemType: String
    ) {
        // Check if search already exists and update timestamp if it does
        val existingSearch = searchDao.getSearchByQueryAndLanguage(query, language)
        
        val searchId = if (existingSearch != null) {
            searchDao.updateTimestamp(query, language, System.currentTimeMillis())
            existingSearch.id
        } else {
            searchDao.insertSearch(
                SearchLocalDto(
                    query = query,
                    language = language
                )
            )
        }
        
        clearExpiredResults()
        searchResultDao.insert(
            SearchResultLocalDto(
                id = searchId,
                itemId = itemId,
                itemType = itemType
            )
        )
    }


    override suspend fun addSearchResults(
        query: String,
        language: String,
        results: List<Pair<Int, String>> 
    ) {
        val existingSearch = searchDao.getSearchByQueryAndLanguage(query, language)
        
        val searchId = if (existingSearch != null) {
            searchDao.updateTimestamp(query, language, System.currentTimeMillis())
            existingSearch.id
        } else {
            searchDao.insertSearch(
                SearchLocalDto(
                    query = query,
                    language = language
                )
            )
        }
        
        val searchResults = results.map { (itemId, itemType) ->
            SearchResultLocalDto(
                id = searchId,
                itemId = itemId,
                itemType = itemType
            )
        }
        
        clearExpiredResults()
        searchResultDao.insertAll(searchResults)
    }


    override suspend fun getCachedResults(
        query: String,
        language: String,
        type: String
    ): List<SearchResultLocalDto> {
        clearExpiredResults()
        
        val search = searchDao.getSearchByQueryAndLanguage(query, language)
        
        if (search == null || isExpired(search.timestamp)) {
            return emptyList()
        }
        
        searchDao.updateTimestamp(query, language, System.currentTimeMillis())
        
        return searchResultDao.getByQueryAndLanguage(query, language, type)
    }

    override suspend fun addActor(actorsLocalDto: ActorsLocalDto) {
        actorDao.insertActor(actorsLocalDto)
    }

    override suspend fun addMovie(moviesLocalDto: MoviesLocalDto) {
        movieDao.insertMovie(moviesLocalDto)
    }

    override suspend fun addTvSeries(tvSeriesLocalDto: TvSeriesLocalDto) {
        seriesDao.insertSeries(tvSeriesLocalDto)
    }

    override suspend fun getActorsByQuery(
        query: String,
        language: String
    ): List<ActorsLocalDto> {
        val cachedResults = getCachedResults(query, language, "actor")
        
        if (cachedResults.isNotEmpty()) {
            return emptyList()
        }
        
        return listOfNotNull(actorDao.getActorByQuery(query))
    }

    override suspend fun getMoviesByQuery(
        query: String,
        language: String
    ): List<MoviesLocalDto> {
        val cachedResults = getCachedResults(query, language, "movie")
        
        if (cachedResults.isNotEmpty()) {
           
            return emptyList()
        }
        
        return movieDao.getFilteredMovies(query = query)
    }

    override suspend fun getTvSeriesByQuery(
        query: String,
        language: String
    ): List<TvSeriesLocalDto> {
        val cachedResults = getCachedResults(query, language, "tv_series")
        
        // If we have cached results, return them
        if (cachedResults.isNotEmpty()) {
           
            return emptyList()
        }

        return emptyList()
    }

   
    override suspend fun clearExpiredResults() {
        val expirationTime = System.currentTimeMillis() - CACHE_EXPIRATION_TIME
        searchResultDao.deleteOldResults(expirationTime)
    }

   
    private fun isExpired(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp > CACHE_EXPIRATION_TIME
    }

       companion object {
        private const val CACHE_EXPIRATION_TIME = 60 * 60 * 1000L 
        private const val TAG = "LocalCachedSearchDataSource"
    }

} 