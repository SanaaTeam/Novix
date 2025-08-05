package com.sanaa.vod.dataSource.local.cache

import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.local.cache.dto.GenreLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto

interface LocalCachedContentDataSource {
    suspend fun cacheMovie(movies: List<MovieLocalDto>, category: Category)
    suspend fun getCachedMovies(category: Category): List<MovieLocalDto>
    suspend fun cacheTvShow(tvShows: List<TvShowLocalDto>, category: Category)
    suspend fun getCachedTvShows(category: Category): List<TvShowLocalDto>
    suspend fun cacheGenres(genres: List<GenreLocalDto>, category: Category)
    suspend fun getCachedGenres(category: Category): List<GenreLocalDto>

    suspend fun clearExpiredCache()
}