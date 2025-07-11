package com.sanaa.search.data.local.datasource

import com.sanaa.search.data.local.dao.SearchDao
import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.dataSource.local.dto.SearchLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.LocalCachedSearchDataSource
import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto

class LocalCachedSearchDataSourceImpl(
    private val searchDao: SearchDao,
    private val searchResultDao: SearchResultDao
) : LocalCachedSearchDataSource {

    override suspend fun addSearchResult(
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
                id = searchId,
                itemId = itemId,
                itemType = itemType
            )
        )
    }

    override suspend fun getCachedResults(
        query: String,
        language: String,
        type: String
    ): List<SearchResultLocalDto> {
        TODO("Not yet implemented")
    }

    override suspend fun addActor(actorsLocalDto: ActorsLocalDto) {
        TODO("Not yet implemented")
    }

    override suspend fun addMovie(moviesLocalDto: MoviesLocalDto) {
        TODO("Not yet implemented")
    }

    override suspend fun addTvSeries(tvSeriesLocalDto: TvSeriesLocalDto) {
        TODO("Not yet implemented")
    }

    override suspend fun getActorsByQuery(
        query: String,
        language: String
    ): List<ActorsLocalDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getMoviesByQuery(
        query: String,
        language: String
    ): List<MoviesLocalDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvSeriesByQuery(
        query: String,
        language: String
    ): List<TvSeriesLocalDto> {
        TODO("Not yet implemented")
    }

    override suspend fun clearExpiredResults() {
        val oneHourAgo = System.currentTimeMillis() - 60 * 60 * 1000
        searchResultDao.deleteOldResults(oneHourAgo)
    }
} 