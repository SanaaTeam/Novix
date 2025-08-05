package com.sanaa.vod.cache

import com.google.common.truth.Truth.assertThat
import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.cache.dao.CachedContentDao
import com.sanaa.vod.cache.dao.CachedContentMetadataDao
import com.sanaa.vod.cache.dao.MovieDao
import com.sanaa.vod.cache.dao.TvShowDao
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto.MediaType
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto
import com.sanaa.vod.util.TimeUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalCachedContentDataSourceImplTest {

    private lateinit var cachedContentDao: CachedContentDao
    private lateinit var cachedContentMetadataDao: CachedContentMetadataDao
    private lateinit var movieDao: MovieDao
    private lateinit var tvShowDao: TvShowDao
    private lateinit var languageProvider: LanguageProvider

    private lateinit var localCachedContentDataSource: LocalCachedContentDataSourceImpl

    @BeforeEach
    fun setUp() {
        cachedContentDao = mockk(relaxed = true)
        cachedContentMetadataDao = mockk(relaxed = true)
        movieDao = mockk(relaxed = true)
        tvShowDao = mockk(relaxed = true)
        languageProvider = mockk(relaxed = true)

        every { languageProvider.getCurrentLanguage() } returns FAKE_LANGUAGE

        localCachedContentDataSource = LocalCachedContentDataSourceImpl(
            cachedContentDao,
            cachedContentMetadataDao,
            movieDao,
            tvShowDao,
            languageProvider
        )
    }

    @Test
    fun `cacheMovie should insert movies and link to cacheContent when caching movies`() {
        runTest {
            coEvery { movieDao.insertAll(any()) } returns Unit
            coEvery { cachedContentMetadataDao.getCachedContentMetadata(any(), any()) } returns null
            coEvery { cachedContentMetadataDao.insertCachedContentMetadata(any()) } returns 10L
            coEvery { cachedContentDao.insert(any()) } returns Unit

            localCachedContentDataSource.cacheMovie(movies, Category.POPULAR)

            coVerify {
                movieDao.insertAll(movies)
                cachedContentDao.insert(match { it.mediaId == movies[0].id && it.mediaType == MediaType.MOVIE.name })
                cachedContentDao.insert(match { it.mediaId == movies[1].id && it.mediaType == MediaType.MOVIE.name })
            }
        }
    }

    @Test
    fun `getCachedMovies should return empty list when no metadata is found`() = runTest {
        coEvery { cachedContentMetadataDao.getCachedContentMetadata(any(), any()) } returns null

        val result = localCachedContentDataSource.getCachedMovies(Category.TOP_RATED)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getCachedMovies should return movies when there is valid metadata`() = runTest {
        coEvery { cachedContentMetadataDao.getCachedContentMetadata(any(), any()) } returns dummyMetadata
        coEvery { cachedContentDao.getCachedContentInfo(any(), any()) } returns cachedContent
        coEvery { movieDao.getMoviesByIds(any()) } returns movies

        val result = localCachedContentDataSource.getCachedMovies(Category.TOP_RATED)

        assertThat(result).isEqualTo(movies)
    }

    @Test
    fun `cacheTvShow should insert tv shows and link to cacheContent when caching tv shows`() =
        runTest {


            coEvery { tvShowDao.insertAll(any()) } returns Unit
            coEvery { cachedContentMetadataDao.getCachedContentMetadata(any(), any()) } returns null
            coEvery { cachedContentMetadataDao.insertCachedContentMetadata(any()) } returns 20L
            coEvery { cachedContentDao.insert(any()) } returns Unit

            localCachedContentDataSource.cacheTvShow(tvShows, Category.UPCOMING)

            coVerify { tvShowDao.insertAll(tvShows) }
            coVerify { cachedContentDao.insert(match { it.mediaId == 2 && it.mediaType == MediaType.TV_SHOW.name }) }
        }

    @Test
    fun `getCachedTvShows should return empty list when no valid metadata`() = runTest {
        coEvery { cachedContentMetadataDao.getCachedContentMetadata(any(), any()) } returns null

        val result = localCachedContentDataSource.getCachedTvShows(Category.UPCOMING)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getCachedTvShows should return data when there is valid metadata`() = runTest {
        coEvery { cachedContentMetadataDao.getCachedContentMetadata(any(), any()) } returns dummyMetadata
        coEvery { cachedContentDao.getCachedContentInfo(any(), any()) } returns cachedContent
        coEvery { tvShowDao.getTvShowsByIds(any()) } returns tvShows

        val result = localCachedContentDataSource.getCachedTvShows(Category.UPCOMING)

        assertThat(result).isEqualTo(tvShows)
    }

    @Test
    fun `clearExpiredCache should delete expired metadata and unreferenced media`() = runTest {
        coEvery { cachedContentMetadataDao.clearExpiredMetadata(any()) } returns Unit
        coEvery { movieDao.deleteUnreferencedMovies() } returns Unit
        coEvery { tvShowDao.deleteUnreferencedTvShows() } returns Unit

        localCachedContentDataSource.clearExpiredCache()

        coVerify {
            cachedContentMetadataDao.clearExpiredMetadata(any())
        }
        coVerify { movieDao.deleteUnreferencedMovies() }
        coVerify { tvShowDao.deleteUnreferencedTvShows() }
    }

//    @Test
//    fun `getCachedContent should return empty list when metadata is expired`() = runTest {
//        val oldTimestamp =
//            TimeUtils.getCurrentTimeStamp() - (CACHE_EXPIRATION_TIME + 1000)
//        val metadata = dummyMetadata.copy(timestamp = oldTimestamp)
//        coEvery { cachedContentMetadataDao.getCachedContentMetadata(any(), any()) } returns metadata
//
//        val result =
//            localCachedContentDataSource.getCachedContent(Category.TOP_RATED, MediaType.MOVIE)
//
//        assertThat(result).isEmpty()
//    }


    private companion object {
        const val FAKE_LANGUAGE = "en"

        val dummyMovie1 = MovieLocalDto(1, "Movie 1", "url", "2020-01-01", 8.5f)
        val dummyMovie2 = MovieLocalDto(1, "Test Movie", "url", null, 7.0f)
        val movies = listOf(dummyMovie1, dummyMovie2)

        val dummyTvShow1 =
            TvShowLocalDto(1, "TV Show", "url", null, 8.0f)

        val dummyTvShow2 =
            TvShowLocalDto(2, "TV Show", "url", "2020-01-01", 9.0f)

        val tvShows = listOf(dummyTvShow1, dummyTvShow2)

        val dummyMetadata = CachedContentMetadataLocalDto(
            id = 1L,
            language = FAKE_LANGUAGE,
            category = Category.TOP_RATED.name,
            timestamp = TimeUtils.getCurrentTimeStamp()
        )

        val cachedContent = listOf(
            CachedContentLocalDto(
                id = dummyMetadata.id,
                mediaId = dummyMovie1.id,
                mediaType = MediaType.MOVIE.name
            ),
            CachedContentLocalDto(
                id = dummyMetadata.id,
                mediaId = dummyMovie2.id,
                mediaType = MediaType.MOVIE.name
            )
        )
    }
}