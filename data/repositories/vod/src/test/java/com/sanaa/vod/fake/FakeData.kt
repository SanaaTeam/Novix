package com.sanaa.vod.fake

import com.sanaa.vod.dataSource.local.search.dto.ActorLocalDto
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.dto.ActorSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.search.response.SearchResponse

object FakeData {
    val ActorsLocalDtoList = listOf(
        ActorLocalDto(id = 1, name = "Tom Hanks", imagePath = "img", language = "en"),
        ActorLocalDto(id = 2, name = "Leonardo Leonardo", imagePath = "img", language = "en")
    )
    val ActorsRemoteDtoList = listOf(
        ActorSearchDto(
            id = 1,
            name = "Tom Hanks",
            profileImagePath = "/path/to/image"
        ),
        ActorSearchDto(
            id = 2,
            name = "sam",
            profileImagePath = "/path/to/image"
        ),
    )
    val actorSearchResponse = SearchResponse(
        page = 1,
        results = ActorsRemoteDtoList,
        totalPages = 1,
        totalResults = ActorsRemoteDtoList.size
    )

    val MoviesLocalDtoList = listOf(
        MovieLocalDto(
            id = 1, "Movie", "2024", null,
            null, null, "en", System.currentTimeMillis()
        ),
        MovieLocalDto(
            id = 1, "Movie2", "2025", null,
            null, null, "en", System.currentTimeMillis()
        ),
    )

    val MoviesRemoteDtoList = listOf(
        MovieSearchDto(
            id = 1, "Movie", "2024", null, null, null,
        ),
        MovieSearchDto(
            id = 1, "Movie2", "2025", null, null, null
        ),
    )

    val MovieSearchResponse = SearchResponse(
        page = 1,
        results = MoviesRemoteDtoList,
        totalPages = 1,
        totalResults = MoviesRemoteDtoList.size
    )

    val TvSeriesLocalDtoList = listOf(
        TvSeriesLocalDto(
            id = 1, "TvSeries", "2025", null,
            null, null, "en", System.currentTimeMillis()
        ),
        TvSeriesLocalDto(
            id = 1, "TvSeries2", "2025", null,
            null, null, "en", System.currentTimeMillis()
        ),
    )

    val TvSeriesRemoteDtoList = listOf(
        TvShowSearchDto(
            id = 1, "TvSeries", "2025", null, null, null,
        ),
        TvShowSearchDto(
            id = 1, "TvSeries2", "2025", null, null, null
        ),
    )

    val TvSeriesSearchResponse = SearchResponse(
        page = 1,
        results = TvSeriesRemoteDtoList,
        totalPages = 1,
        totalResults = TvSeriesRemoteDtoList.size
    )
}