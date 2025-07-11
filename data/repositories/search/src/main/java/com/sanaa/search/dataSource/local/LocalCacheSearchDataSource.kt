package com.sanaa.search.dataSource.local

import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto

interface LocalCacheSearchDataSource {

    suspend fun cacheSearchResult(query: String, language: String, itemId: Int, itemType: String)
    suspend fun getCachedResults(
        query: String,
        language: String,
        type: String
    ): List<SearchResultLocalDto>

    suspend fun cacheActor(actorsLocalDto: ActorsLocalDto)
    suspend fun cacheMovie(moviesLocalDto: MoviesLocalDto)
    suspend fun cacheTvSeries(tvSeriesLocalDto: TvSeriesLocalDto)

    suspend fun getActorsByQuery(query: String, language: String): List<ActorsLocalDto>
    suspend fun getMoviesByQuery(query: String, language: String): List<MoviesLocalDto>
    suspend fun getTvSeriesByQuery(query: String, language: String): List<TvSeriesLocalDto>

    suspend fun clearExpiredCache(expirationTime: Long)
}