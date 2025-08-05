package com.sanaa.vod.cache

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.cache.dao.CachedContentDao
import com.sanaa.vod.cache.dao.CachedContentMetadataDao
import com.sanaa.vod.cache.dao.GenreDao
import com.sanaa.vod.cache.dao.MovieDao
import com.sanaa.vod.cache.dao.TvShowDao
import com.sanaa.vod.dataSource.local.cache.LocalCachedContentDataSource
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.ContentType
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.local.cache.dto.GenreLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto
import javax.inject.Inject

class LocalCachedContentDataSourceImpl @Inject constructor(
    private val cachedContentDao: CachedContentDao,
    private val cachedContentMetadataDao: CachedContentMetadataDao,
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao,
    private val genreDao: GenreDao,
    private val languageProvider: LanguageProvider
) : LocalCachedContentDataSource {

    private val currentLanguage: String
        get() = languageProvider.getCurrentLanguage()

    override suspend fun cacheMovie(movie: List<MovieLocalDto>, category: Category) {
        movieDao.insertAll(movie).also {
            movie.forEach { movie ->
                cacheContent(
                    mediaId = movie.id,
                    contentType = ContentType.MOVIE,
                    category = category
                )
            }
        }
    }

    override suspend fun getCachedMovies(category: Category): List<MovieLocalDto> {
        val cachedMoviesInfo = getCachedContent(category, ContentType.MOVIE)

        if (cachedMoviesInfo.isEmpty()) {
            return emptyList()
        }

        val moviesIds = cachedMoviesInfo.map { it.itemId }
        return movieDao.getMoviesByIds(moviesIds)
    }

    override suspend fun cacheTvShow(tvShow: List<TvShowLocalDto>, category: Category) {
        tvShowDao.insertAll(tvShow).also {
            tvShow.forEach { tvShow ->
                cacheContent(
                    mediaId = tvShow.id,
                    contentType = ContentType.TV_SHOW,
                    category = category
                )
            }
        }
    }

    override suspend fun getCachedTvShows(category: Category): List<TvShowLocalDto> {
        val cachedTvShowsInfo = getCachedContent(category, ContentType.TV_SHOW)

        if (cachedTvShowsInfo.isEmpty()) {
            return emptyList()
        }

        val ids = cachedTvShowsInfo.map { it.itemId }
        return tvShowDao.getTvShowsByIds(ids)
    }

    override suspend fun cacheGenres(
        genres: List<GenreLocalDto>,
        category: Category
    ) {
        genreDao.insertAll(genres).also {
            genres.forEach { genre ->
                cacheContent(
                    mediaId = genre.id,
                    contentType = ContentType.GENRE,
                    category = category
                )
            }
        }
    }

    override suspend fun getCachedGenres(category: Category): List<GenreLocalDto> {
        val cachedGenreInfo = getCachedContent(category, ContentType.GENRE)

        if (cachedGenreInfo.isEmpty()) {
            return emptyList()
        }

        val moviesIds = cachedGenreInfo.map { it.itemId }
        return genreDao.getGenreByIds(moviesIds)
    }

    private suspend fun getCachedContent(
        category: Category,
        contentType: ContentType
    ): List<CachedContentLocalDto> {

        clearExpiredCache()

        val metadata = cachedContentMetadataDao.getCachedContentMetadata(
            category = category.name,
            language = currentLanguage
        )

        if (metadata == null) {
            return emptyList()
        }

        return cachedContentDao.getCachedContentInfo(metadata.id, contentType.name)
    }

    private suspend fun cacheContent(
        mediaId: Int,
        contentType: ContentType,
        category: Category
    ) {

        val existingMetadata = cachedContentMetadataDao.getCachedContentMetadata(
            category = category.name,
            language = currentLanguage
        )

        val metadataId = existingMetadata?.id
            ?: cachedContentMetadataDao.insertCachedContentMetadata(
                CachedContentMetadataLocalDto(
                    language = currentLanguage,
                    category = category.name,
                )
            )

        cachedContentDao.insert(
            CachedContentLocalDto(
                id = metadataId,
                itemId = mediaId,
                contentType = contentType.name
            )
        )
    }

    override suspend fun clearExpiredCache() {
        val oneDayAgo = System.currentTimeMillis() - CACHE_EXPIRATION_TIME

        cachedContentMetadataDao.clearExpiredMetadata(oneDayAgo)

        movieDao.deleteUnreferencedMovies()
        tvShowDao.deleteUnreferencedTvShows()
        genreDao.deleteUnreferencedGenres()
    }

    companion object {
        const val CACHE_EXPIRATION_TIME = 1000 * 60 * 60 * 24L // 24 hours in milliseconds
    }
}