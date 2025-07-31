package com.sanaa.vod.datasource

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.local.search.dto.ActorLocalDto
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.SearchLocalDto
import com.sanaa.vod.dataSource.local.search.dto.SearchResultLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.fake.FakeData
import com.sanaa.vod.fake.FakeData.movieList
import com.sanaa.vod.search.search_result.LocalCachedSearchDataSourceImpl
import com.sanaa.vod.search.search_result.dao.ActorDao
import com.sanaa.vod.search.search_result.dao.MovieDao
import com.sanaa.vod.search.search_result.dao.SearchDao
import com.sanaa.vod.search.search_result.dao.SearchResultDao
import com.sanaa.vod.search.search_result.dao.SeriesDao
import com.sanaa.vod.util.TimeUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class LocalSearchDataSourceImplTest {
    private lateinit var localCachedSearchDataSourceImpl: LocalCachedSearchDataSourceImpl

    private val searchDao: SearchDao = mockk(relaxed = true)
    private val searchResultDao: SearchResultDao = mockk(relaxed = true)
    private val actorDao: ActorDao = mockk(relaxed = true)
    private val movieDao: MovieDao = mockk(relaxed = true)
    private val seriesDao: SeriesDao = mockk(relaxed = true)
    private val languageProvider = mockk<LanguageProvider>(relaxed = true)

    @BeforeEach
    fun setUp() {
        coEvery { languageProvider.getCurrentLanguage() } returns "en"
        localCachedSearchDataSourceImpl = LocalCachedSearchDataSourceImpl(
            searchDao = searchDao,
            searchResultDao = searchResultDao,
            actorDao = actorDao,
            movieDao = movieDao,
            seriesDao = seriesDao,
            languageProvider = languageProvider
        )
    }

    @Test
    fun `cacheSearchResult should insert search and result when no existing search`() = runTest {
        val query = "test query"
        val itemId = 123
        val itemType = "movie"
        val insertedId = 1

        coEvery { searchDao.getSearchByQueryAndLanguage(query, any()) } returns null
        coEvery { searchDao.insertSearch(any()) } returns insertedId.toLong()
        coEvery { searchDao.updateTimestamp(any(), any(), any()) } returns Unit
        coEvery { searchResultDao.insert(any()) } returns Unit

        localCachedSearchDataSourceImpl.cacheSearchResult(query, itemId, itemType)

        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 1) { searchDao.insertSearch(match { it.query == query && it.language == "en" }) }
        coVerify(exactly = 1) { searchResultDao.insert(match { it.id == insertedId && it.itemId == itemId && it.itemType == itemType }) }
    }

    @Test
    fun `cacheSearchResult should update timestamp when existing search exists`() = runTest {
        val query = "existing query"
        val itemId = 123
        val itemType = "movie"
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val existingSearch = SearchLocalDto(
            id = 42, query = query, language = "en", timestamp = currentTimestamp
        )

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns existingSearch
        coEvery { searchDao.updateTimestamp(query, "en", any()) } returns Unit
        coEvery { searchResultDao.insert(any()) } returns Unit

        localCachedSearchDataSourceImpl.cacheSearchResult(query, itemId, itemType)

        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 0) { searchDao.insertSearch(any()) }
        coVerify(exactly = 1) { searchDao.updateTimestamp(query, "en", any()) }
        coVerify(exactly = 1) { searchResultDao.insert(match { it.id == existingSearch.id && it.itemId == itemId && it.itemType == itemType }) }
    }

    @Test
    fun `getCachedResults should return results from searchResultDao`() = runTest {
        val query = "test query"
        val type = "movie"
        val expected = listOf(SearchResultLocalDto(1, 123, "movie"))
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()

        coEvery {
            searchDao.getSearchByQueryAndLanguage(query, "en")
        } returns SearchLocalDto(1, query, "en", currentTimestamp)
        coEvery {
            searchResultDao.getByQueryAndLanguage(query, "en", type)
        } returns expected

        val result = localCachedSearchDataSourceImpl.getCachedResults(query, type)

        assertEquals(expected, result)
        coVerify { searchResultDao.getByQueryAndLanguage(query, "en", type) }
    }

    @Test
    fun `getCachedResults returns empty list when search is expired`() = runTest {
        val query = "expired query"
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val expiredTimestamp =
            currentTimestamp - (LocalCachedSearchDataSourceImpl.CACHE_EXPIRATION_TIME + 1000)
        val expiredSearch = SearchLocalDto(
            id = 1, query = query, language = "en", timestamp = expiredTimestamp
        )

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns expiredSearch

        val result = localCachedSearchDataSourceImpl.getCachedResults(query, "movie")

        assertEquals(emptyList(), result)
        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 0) { searchDao.updateTimestamp(any(), any(), any()) }
    }

    @Test
    fun `getCachedResults returns empty list when search is null`() = runTest {
        val query = "no search"

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null

        val result = localCachedSearchDataSourceImpl.getCachedResults(query, "movie")

        assertEquals(emptyList(), result)
        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 0) { searchDao.updateTimestamp(any(), any(), any()) }
    }

    @Test
    fun `clearExpiredCache should call deleteOldResults`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val expiredTime = currentTimestamp - 3600000L

        localCachedSearchDataSourceImpl.clearExpiredCache(expiredTime)

        coVerify { searchResultDao.deleteOldResults(expiredTime) }
    }

    @Test
    fun `getActorsByQuery returns cached results`() = runTest {
        val query = "actor query"
        val cachedResults = listOf(SearchResultLocalDto(1, 123, "actor"))
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val actor = ActorLocalDto(123, "Actor Name", "", "en", currentTimestamp, 0)

        coEvery {
            searchDao.getSearchByQueryAndLanguage(query, "en")
        } returns SearchLocalDto(1, query, "en", currentTimestamp)
        coEvery {
            searchResultDao.getByQueryAndLanguage(query, "en", "actor")
        } returns cachedResults
        coEvery {
            actorDao.getActorsByQuery("123")
        } returns listOf(actor)

        val result = localCachedSearchDataSourceImpl.getActorsByQuery(query)

        assertEquals(listOf(actor), result)
        coVerify { actorDao.getActorsByQuery("123") }
    }

    @Test
    fun `getActorsByQuery returns direct actorDao result if no cached results`() = runTest {
        val query = "actor query"
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val itemType = "movie"
        val listOf = listOf(
            ActorLocalDto(
                id = 99,
                name = "Actor",
                imagePath = "",
                language = "en",
                timestamp = currentTimestamp,
                gender = 0
            )
        )
        val listOf3 = listOf(
            SearchResultLocalDto(1, 1, itemType),
            SearchResultLocalDto(1, 2, itemType),
        )

        coEvery {
            searchDao.getSearchByQueryAndLanguage(query, "en")
        } returns SearchLocalDto(1, query, "en", FakeData.currentTimestamp)
        coEvery {
            movieDao.getPagedMoviesByIds(any(), any(), any())
        } returns movieList
        coEvery {
            searchResultDao.getByQueryAndLanguage(query, any(), any())
        } returns listOf3
        coEvery {
            actorDao.getActorsByQuery(query)
        } returns listOf

        localCachedSearchDataSourceImpl.getActorsByQuery(query)
        coVerify { actorDao.getActorsByQuery(any()) }
    }

    @Test
    fun `getTvSeriesByQuery returns empty list if no cached results`() = runTest {
        val query = "tv query"

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null

        val result = localCachedSearchDataSourceImpl.getTvSeriesByQuery(query, 20, 0)

        assertEquals(emptyList(), result)
    }

    @Test
    fun `insertMovie should call insertMovie from movieDao`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val movie = MovieLocalDto(
            id = 1,
            title = "title",
            imagePath = "",
            releaseDate = "2002-10-10",
            genres = null,
            imdbRating = 2.1f,
            language = "en",
            timestamp = currentTimestamp,
        )
        coEvery { movieDao.insertMovie(movie) } returns Unit

        localCachedSearchDataSourceImpl.cacheMovie(movie)

        coVerify { movieDao.insertMovie(movie) }
    }

    @Test
    fun `insertActor should call insertActor from actorDao`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val actor = ActorLocalDto(
            id = 1,
            name = "name",
            imagePath = "",
            language = "en",
            timestamp = currentTimestamp,
            gender = 0
        )
        coEvery { actorDao.insertActor(actor) } returns Unit

        localCachedSearchDataSourceImpl.cacheActor(actor)

        coVerify { actorDao.insertActor(actor) }
    }

    @Test
    fun `insertSeries should call insertSeries from seriesDao`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val series = TvSeriesLocalDto(
            id = 1,
            title = "title",
            imagePath = "",
            releaseDate = "2002-10-10",
            genres = null,
            imdbRating = 2.1f,
            language = "en",
            timestamp = currentTimestamp,
        )
        coEvery { seriesDao.insertSeries(series) } returns Unit

        localCachedSearchDataSourceImpl.cacheTvSeries(series)

        coVerify { seriesDao.insertSeries(series) }
    }

    // ========== PAGINATION TESTS ==========


    @Test
    fun `getMoviesByQuery shouldReturnEmptyList whenNoResults`() = runTest {
        // Given
        val query = "NonExistentMovie"
        val limit = 20
        val offset = 0

        coEvery { movieDao.getPagedMoviesByIds(any(), limit, offset) } returns emptyList()

        // When
        val result = localCachedSearchDataSourceImpl.getMoviesByQuery(query, limit, offset)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `pagination shouldHandleZeroLimit`() = runTest {
        // Given
        val query = "Test"
        val limit = 0
        val offset = 0

        coEvery { actorDao.getPagedActorsByIds(any(), limit, offset) } returns emptyList()
        coEvery { movieDao.getPagedMoviesByIds(any(), limit, offset) } returns emptyList()
        coEvery { seriesDao.getPagedTvSeriesByIds(any(), limit, offset) } returns emptyList()

        // When
        val actorsResult =
            localCachedSearchDataSourceImpl.getPagedActorsByQuery(query, limit, offset)
        val moviesResult = localCachedSearchDataSourceImpl.getMoviesByQuery(query, limit, offset)
        val seriesResult = localCachedSearchDataSourceImpl.getTvSeriesByQuery(query, limit, offset)

        // Then
        assertTrue(actorsResult.isEmpty())
        assertTrue(moviesResult.isEmpty())
        assertTrue(seriesResult.isEmpty())
    }

    @Test
    fun `pagination shouldHandleNegativeOffset`() = runTest {
        // Given
        val query = "Test"
        val limit = 20
        val offset = -10

        coEvery { actorDao.getPagedActorsByIds(any(), limit, offset) } returns emptyList()

        // When
        val result = localCachedSearchDataSourceImpl.getPagedActorsByQuery(query, limit, offset)

        // Then
        assertTrue(result.isEmpty())
        coVerify { actorDao.getPagedActorsByIds(any(), limit, offset) }
    }

    @Test
    fun `pagination shouldHandleVeryLargeLimit`() = runTest {
        // Given
        val query = "Test"
        val limit = 1000
        val offset = 0
        val largeResult = List(1000) { index ->
            ActorLocalDto(index, "Actor $index", "img$index", "en", System.currentTimeMillis(), 0)
        }

        coEvery { actorDao.getPagedActorsByIds(any(), limit, offset) } returns largeResult

        // When
        val result = localCachedSearchDataSourceImpl.getPagedActorsByQuery(query, limit, offset)

        // Then
        assertEquals(1000, result.size)
        coVerify { actorDao.getPagedActorsByIds(any(), limit, offset) }
    }

    @Test
    fun `pagination shouldHandleConsecutivePages`() = runTest {
        // Given
        val query = "Test"
        val pageSize = 10

        val page1Actors = List(10) { index ->
            ActorLocalDto(index, "Actor $index", "img$index", "en", System.currentTimeMillis(), 0)
        }
        val page2Actors = List(10) { index ->
            ActorLocalDto(
                index + 10,
                "Actor ${index + 10}",
                "img${index + 10}",
                "en",
                System.currentTimeMillis(),
                0
            )
        }

        coEvery { actorDao.getPagedActorsByIds(any(), pageSize, 0) } returns page1Actors
        coEvery { actorDao.getPagedActorsByIds(any(), pageSize, 10) } returns page2Actors

        // When
        val result1 = localCachedSearchDataSourceImpl.getPagedActorsByQuery(query, pageSize, 0)
        val result2 = localCachedSearchDataSourceImpl.getPagedActorsByQuery(query, pageSize, 10)

        // Then
        assertEquals(10, result1.size)
        assertEquals(10, result2.size)
        assertNotEquals(result1, result2)
        assertTrue(result1.all { it.id < 10 })
        assertTrue(result2.all { it.id >= 10 })
    }
}