package com.sanaa.vod.fake

import com.sanaa.vod.dataSource.remote.dto.search.ActorSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.MovieSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.response.SearchResponse

object FakeData {

    val ActorsRemoteDtoList = listOf(
        ActorSearchDto(
            id = 1,
            name = "Tom Hanks",
            profileImagePath = "/path/to/image",
            gender = 0,
            knownForDepartment = "Acting"
        ),
        ActorSearchDto(
            id = 2,
            name = "sam",
            profileImagePath = "/path/to/image",
            gender = 0,
            knownForDepartment = "Acting"
        ),
    )
    val actorSearchResponse = SearchResponse(
        page = 1,
        results = ActorsRemoteDtoList,
        totalPages = 1,
        totalResults = ActorsRemoteDtoList.size
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

    val tvShowSearchDtos = listOf(
        TvShowSearchDto(
            id = 1, "TvSeries", "2025", null, null, null,
        ),
        TvShowSearchDto(
            id = 1, "TvSeries2", "2025", null, null, null
        ),
    )

    val tvShowSearchResponse = SearchResponse(
        page = 1,
        results = tvShowSearchDtos,
        totalPages = 1,
        totalResults = tvShowSearchDtos.size
    )
}