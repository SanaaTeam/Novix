package com.sanaa.search.fake

import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.search_result.LocalCachedSearchDataSourceImpl

object FakeData {
    val currentTimestamp = LocalCachedSearchDataSourceImpl.CACHE_EXPIRATION_TIME + 1000
    val movieList = listOf(
        MovieLocalDto(1, "Movie1", "img1", 2005, "28,80", 8.2f, "en", System.currentTimeMillis()),
        MovieLocalDto(2, "Movie2", "img2", 2008, "28,80,53", 9.0f, "en", System.currentTimeMillis())
    )

}