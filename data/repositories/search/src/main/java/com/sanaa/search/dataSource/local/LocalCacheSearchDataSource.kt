package com.sanaa.search.dataSource.local

import com.sanaa.search.dataSource.local.dto.ActorLocalDto
import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto

interface LocalCacheSearchDataSource {

    suspend fun cacheSearchResult(query: String, itemId: Int, itemType: String)
    suspend fun getCachedResults(query: String, type: String): List<SearchResultLocalDto>

    suspend fun cacheActor(actorLocalDto: ActorLocalDto)
    suspend fun cacheMovie(movieLocalDto: MovieLocalDto)
    suspend fun cacheTvSeries(tvSeriesLocalDto: TvSeriesLocalDto)

    suspend fun getActorsByQuery(query: String): List<ActorLocalDto>
    suspend fun getMoviesByQuery(query: String): List<MovieLocalDto>
    suspend fun getTvSeriesByQuery(query: String): List<TvSeriesLocalDto>

    suspend fun clearExpiredCache(expirationTime: Long)
}