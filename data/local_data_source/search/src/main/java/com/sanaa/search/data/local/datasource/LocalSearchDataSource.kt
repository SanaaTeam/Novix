package com.sanaa.search.data.local.datasource

import com.sanaa.search.data.local.entity.SearchResultLocalDto

interface LocalSearchDataSource {

    suspend fun saveSearchResult(
        query: String,
        language: String,
        itemId: Int,
        itemType: String
    )

    suspend fun getCachedResults(
        query: String,
        language: String
    ): List<SearchResultLocalDto>

    suspend fun clearExpiredResults()
} 