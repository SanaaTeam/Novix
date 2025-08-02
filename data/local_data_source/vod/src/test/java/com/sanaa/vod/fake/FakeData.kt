package com.sanaa.vod.fake

import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.search.LocalCachedSearchDataSourceImpl

object FakeData {
    val currentTimestamp = LocalCachedSearchDataSourceImpl.CACHE_EXPIRATION_TIME + 1000
    val movieList = listOf(
        MovieLocalDto(
            1, "Movie1", "img1", "2002-10-10", "28,80", 8.2f, "en", System.currentTimeMillis()
        ), MovieLocalDto(
            2, "Movie2", "img2", "2002-10-10", "28,80,53", 9.0f, "en", System.currentTimeMillis()
        )
    )

}