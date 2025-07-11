package com.sanaa.search.data.local.datasource

import com.sanaa.search.data.local.dao.SearchDao
import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.data.local.entity.SearchLocalDto
import com.sanaa.search.data.local.entity.SearchResultLocalDto

class LocalSearchDataSourceImpl(
    private val searchDao: SearchDao,
    private val searchResultDao: SearchResultDao
) : LocalSearchDataSource {

    override suspend fun saveSearchResult(
        query: String,
        language: String,
        itemId: Int,
        itemType: String
    ) {
        val searchId = searchDao.insertSearch(
            SearchLocalDto(
                query = query,
                language = language
            )
        )
        
        searchResultDao.insert(
            SearchResultLocalDto(
                searchId = searchId,
                itemId = itemId,
                itemType = itemType
            )
        )
    }

    override suspend fun getCachedResults(
        query: String,
        language: String
    ): List<SearchResultLocalDto> {
        return searchResultDao.getByQueryAndLanguage(query, language)
    }

    override suspend fun clearExpiredResults() {
        val oneHourAgo = System.currentTimeMillis() - 60 * 60 * 1000
        searchResultDao.deleteOldResults(oneHourAgo)
    }
} 