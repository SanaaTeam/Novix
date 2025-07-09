package com.sanaa.search.data.local.datasource

import com.sanaa.search.data.local.entity.SearchResultEntity

interface LocalSearchDataSource {

    suspend fun saveSearchResults(
        query: String,
        language: String,
        results: List<SearchResultEntity>
    )

    suspend fun getCachedResults(
        query: String,
        language: String
    ): List<SearchResultEntity>

    suspend fun clearExpiredResults()
} 