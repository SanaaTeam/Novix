package com.sanaa.search.data.local.datasource

import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.data.local.entity.SearchResultEntity

class LocalSearchDataSourceImpl(
    private val dao: SearchResultDao
) : LocalSearchDataSource {

    override suspend fun saveSearchResults(query: String, language: String, results: List<SearchResultEntity>) {
        dao.insertAll(results)
    }

    override suspend fun getCachedResults(query: String, language: String): List<SearchResultEntity> {
        return dao.getByQueryAndLanguage(query, language)
    }

    override suspend fun clearExpiredResults() {
        val oneHourAgo = System.currentTimeMillis() - 60 * 60 * 1000
        dao.deleteOldResults(oneHourAgo)
    }
} 