package com.sanaa.search.dataSource.local

import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto

interface LocalCachedSearchDataSource {

    suspend fun addSearchResult(query: String, language: String, itemId: Int, itemType: String)
    suspend fun getCachedResults(
        query: String,
        language: String,
        type: String
    ): List<SearchResultLocalDto>

    suspend fun addActor(actorsLocalDto: ActorsLocalDto)
    suspend fun addMovie(moviesLocalDto: MoviesLocalDto)
    suspend fun addTvSeries(tvSeriesLocalDto: TvSeriesLocalDto)

    suspend fun getActorsByQuery(query: String, language: String): List<ActorsLocalDto>
    suspend fun getMoviesByQuery(query: String, language: String): List<MoviesLocalDto>
    suspend fun getTvSeriesByQuery(query: String, language: String): List<TvSeriesLocalDto>

    suspend fun clearExpiredResults()
}