package com.sanaa.search.dataSource.local

import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto

interface LocalCacheSearchDataSource {

    suspend fun cacheSearchResult(query: String, itemId: Int, itemType: String)
    suspend fun getCachedResults(query: String, type: String): List<SearchResultLocalDto>

    suspend fun cacheActor(actorsLocalDto: ActorsLocalDto)
    suspend fun cacheMovie(moviesLocalDto: MoviesLocalDto)
    suspend fun cacheTvSeries(tvSeriesLocalDto: TvSeriesLocalDto)

    suspend fun getActorsByQuery(query: String): List<ActorsLocalDto>
    suspend fun getMoviesByQuery(query: String): List<MoviesLocalDto>
    suspend fun getTvSeriesByQuery(query: String): List<TvSeriesLocalDto>

    suspend fun clearExpiredCache(expirationTime: Long)
} 