package com.sanaa.vod.dataSource.local.search

import com.sanaa.vod.dataSource.local.search.dto.ActorLocalDto
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.SearchResultLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto

interface LocalCacheSearchDataSource {

    suspend fun cacheSearchResult(query: String, itemId: Int, itemType: String)
    suspend fun getCachedResults(query: String, type: String): List<SearchResultLocalDto>

    suspend fun cacheActor(actorLocalDto: ActorLocalDto)
    suspend fun cacheMovie(movieLocalDto: MovieLocalDto)
    suspend fun cacheTvSeries(tvSeriesLocalDto: TvSeriesLocalDto)

    suspend fun getActorsByQuery(query: String): List<ActorLocalDto>
    suspend fun getMoviesByQuery(query: String, limit: Int, offset: Int): Pair<Int, List<MovieLocalDto>>
    suspend fun getTvSeriesByQuery(query: String, limit: Int, offset: Int): Pair<Int, List<TvSeriesLocalDto>>

    suspend fun clearExpiredCache(expirationTime: Long)
    suspend fun getPagedActorsByQuery(query: String, limit: Int, offset: Int): Pair<Int, List<ActorLocalDto>>
}