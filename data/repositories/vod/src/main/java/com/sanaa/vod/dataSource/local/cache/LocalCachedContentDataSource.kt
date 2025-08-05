package com.sanaa.vod.dataSource.local.cache

import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto

interface LocalCachedContentDataSource {
    suspend fun cacheMovie(movie: List<MovieLocalDto>, category: Category)
    suspend fun getCachedMovies(category: Category): List<MovieLocalDto>
    suspend fun cacheTvShow(tvShow: List<TvShowLocalDto>, category: Category)
    suspend fun getCachedTvShows(category: Category): List<TvShowLocalDto>

    suspend fun clearExpiredCache()
}