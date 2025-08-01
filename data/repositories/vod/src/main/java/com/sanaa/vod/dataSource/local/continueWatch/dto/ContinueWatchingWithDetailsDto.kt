package com.sanaa.vod.dataSource.local.continueWatch.dto

import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto

data class ContinueWatchingWithDetailsDto(
    val continueWatchingDto: ContinueWatchingLocalDto,
    val movieDetails: MovieLocalDto? = null,
    val seriesDetails: TvSeriesLocalDto? = null
)