package com.sanaa.search.search_result.local.datasource

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.search.dataSource.local.dto.ActorLocalDto
import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.dataSource.local.dto.SearchLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.search_result.LocalCachedSearchDataSourceImpl
import com.sanaa.search.search_result.dao.ActorDao
import com.sanaa.search.search_result.dao.MovieDao
import com.sanaa.search.search_result.dao.SearchDao
import com.sanaa.search.search_result.dao.SearchResultDao
import com.sanaa.search.search_result.dao.SeriesDao
import com.sanaa.search.util.TimeUtils
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
    private lateinit var dataSource: LocalCachedSearchDataSourceImpl

    private val searchDao: SearchDao = mockk(relaxed = true)
    private val searchResultDao: SearchResultDao = mockk(relaxed = true)
    private val actorDao: ActorDao = mockk(relaxed = true)
    private val movieDao: MovieDao = mockk(relaxed = true)
    private val seriesDao: SeriesDao = mockk(relaxed = true)
    private val languageProvider = mockk<LanguageProvider>(relaxed = true)

    @BeforeEach
    fun setUp() {
        coEvery { languageProvider.getCurrentLanguage() } returns "en"
        dataSource = LocalCachedSearchDataSourceImpl(
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

        dataSource.cacheSearchResult(query, itemId, itemType)

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
            id = 42,
            query = query,
            language = "en",
            timestamp = currentTimestamp
        )

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns existingSearch
        coEvery { searchDao.updateTimestamp(query, "en", any()) } returns Unit
        coEvery { searchResultDao.insert(any()) } returns Unit

        dataSource.cacheSearchResult(query, itemId, itemType)

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

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns SearchLocalDto(
            1,
            query,
            "en",
            currentTimestamp
        )
        coEvery { searchResultDao.getByQueryAndLanguage(query, "en", type) } returns expected

        val result = dataSource.getCachedResults(query, type)

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
            id = 1,
            query = query,
            language = "en",
            timestamp = expiredTimestamp
        )

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns expiredSearch

        val result = dataSource.getCachedResults(query, "movie")

        assertEquals(emptyList(), result)
        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 0) { searchDao.updateTimestamp(any(), any(), any()) }
    }

    @Test
    fun `getCachedResults returns empty list when search is null`() = runTest {
        val query = "no search"

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null

        val result = dataSource.getCachedResults(query, "movie")

        assertEquals(emptyList(), result)
        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 0) { searchDao.updateTimestamp(any(), any(), any()) }
    }

    @Test
    fun `clearExpiredCache should call deleteOldResults`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val expiredTime = currentTimestamp - 3600000L

        dataSource.clearExpiredCache(expiredTime)

        coVerify { searchResultDao.deleteOldResults(expiredTime) }
    }

    @Test
    fun `getActorsByQuery returns cached results`() = runTest {
        val query = "actor query"
        val cachedResults = listOf(SearchResultLocalDto(1, 123, "actor"))
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val actor = ActorLocalDto(123, "Actor Name", "", "en", currentTimestamp)

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns SearchLocalDto(
            1,
            query,
            "en",
            currentTimestamp
        )
        coEvery {
            searchResultDao.getByQueryAndLanguage(
                query,
                "en",
                "actor"
            )
        } returns cachedResults
        coEvery { actorDao.getActorsByQuery("123") } returns listOf(actor)

        val result = dataSource.getActorsByQuery(query)

        assertEquals(listOf(actor), result)
        coVerify { actorDao.getActorsByQuery("123") }
    }

    @Test
    fun `getActorsByQuery returns direct actorDao result if no cached results`() = runTest {
        val query = "actor query"
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null
        coEvery { actorDao.getActorsByQuery(query) } returns listOf(
            ActorLocalDto(
                id = 99,
                name = "Actor",
                imagePath = "",
                language = "en",
                timestamp = currentTimestamp
            )
        )

        val result = dataSource.getActorsByQuery(query)

        assertEquals(1, result.size)
        coVerify(exactly = 1) { actorDao.getActorsByQuery(query) }
    }

    @Test
    fun `getMoviesByQuery returns cached results`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val query = "movie query"
        val cachedResults = listOf(SearchResultLocalDto(1, 123, "movie"))
        val movies =
            listOf(MovieLocalDto(123, "Movie1", "", 2020, null, 7.5f, "en", currentTimestamp))

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns SearchLocalDto(
            1,
            query,
            "en",
            currentTimestamp
        )
        coEvery {
            searchResultDao.getByQueryAndLanguage(
                query,
                "en",
                "movie"
            )
        } returns cachedResults
        coEvery { movieDao.getFilteredMovies("123", 20, 0) } returns movies

        val result = dataSource.getMoviesByQuery(query, 20, 0)

        assertEquals(movies, result)
    }

    @Test
    fun `getMoviesByQuery returns direct movieDao results if no cached results`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val query = "movie query"
        val movieList = listOf(
            MovieLocalDto(1, "Movie1", "", 2020, null, 7.5f, "en", currentTimestamp)
        )

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null
        coEvery { movieDao.getFilteredMovies(query, 20, 0) } returns movieList

        val result = dataSource.getMoviesByQuery(query, 20, 0)

        assertEquals(movieList, result)
        coVerify(exactly = 1) { movieDao.getFilteredMovies(query, 20, 0) }
    }

    @Test
    fun `getTvSeriesByQuery returns cached results`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val query = "tv query"
        val cachedResults = listOf(SearchResultLocalDto(1, 123, "tv_series"))
        val series =
            listOf(TvSeriesLocalDto(123, "Series1", "", 2020, null, 8.1f, "en", currentTimestamp))

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns SearchLocalDto(
            1,
            query,
            "en",
            currentTimestamp
        )
        coEvery {
            searchResultDao.getByQueryAndLanguage(
                query,
                "en",
                "tv_series"
            )
        } returns cachedResults
        coEvery { seriesDao.getFilteredSeries("123", 20, 0) } returns series

        val result = dataSource.getTvSeriesByQuery(query, 20, 0)

        assertEquals(series, result)
    }

    @Test
    fun `getTvSeriesByQuery returns empty list if no cached results`() = runTest {
        val query = "tv query"

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null

        val result = dataSource.getTvSeriesByQuery(query, 20, 0)

        assertEquals(emptyList(), result)
    }

    @Test
    fun `insertMovie should call insertMovie from movieDao`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val movie = MovieLocalDto(
            id = 1,
            title = "title",
            imagePath = "",
            releaseYear = 10,
            genres = null,
            imdbRating = 2.1f,
            language = "en",
            timestamp = currentTimestamp,
        )
        coEvery { movieDao.insertMovie(movie) } returns Unit

        dataSource.cacheMovie(movie)

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
        )
        coEvery { actorDao.insertActor(actor) } returns Unit

        dataSource.cacheActor(actor)

        coVerify { actorDao.insertActor(actor) }
    }

    @Test
    fun `insertSeries should call insertSeries from seriesDao`() = runTest {
        val currentTimestamp = TimeUtils.getCurrentTimeStamp()
        val series = TvSeriesLocalDto(
            id = 1,
            title = "title",
            imagePath = "",
            releaseYear = 10,
            genres = null,
            imdbRating = 2.1f,
            language = "en",
            timestamp = currentTimestamp,
        )
        coEvery { seriesDao.insertSeries(series) } returns Unit

        dataSource.cacheTvSeries(series)

        coVerify { seriesDao.insertSeries(series) }
    }

    // ========== PAGINATION TESTS ==========

    @Test
    fun `getPagedActorsByQuery_shouldCallActorDaoWithCorrectParameters`() = runTest {
        // Given
        val query = "Tom"
        val limit = 20
        val offset = 40
        val expectedActors = listOf(
            ActorLocalDto(1, "Tom Hanks", "img1", "en", System.currentTimeMillis()),
            ActorLocalDto(2, "Tom Cruise", "img2", "en", System.currentTimeMillis())
        )

        coEvery { actorDao.getPagedActorsByQuery(query, limit, offset) } returns expectedActors

        // When
        val result = dataSource.getPagedActorsByQuery(query, limit, offset)

        // Then
        assertEquals(expectedActors, result)
        coVerify { actorDao.getPagedActorsByQuery(query, limit, offset) }
    }

    @Test
    fun `getPagedActorsByQuery_shouldReturnEmptyList_whenNoResults`() = runTest {
        // Given
        val query = "NonExistentActor"
        val limit = 20
        val offset = 0

        coEvery { actorDao.getPagedActorsByQuery(query, limit, offset) } returns emptyList()

        // When
        val result = dataSource.getPagedActorsByQuery(query, limit, offset)

        // Then
        assertTrue(result.isEmpty())
        coVerify { actorDao.getPagedActorsByQuery(query, limit, offset) }
    }

    @Test
    fun `getPagedActorsByQuery_shouldHandleLargeOffset`() = runTest {
        // Given
        val query = "Tom"
        val limit = 20
        val offset = 1000

        coEvery { actorDao.getPagedActorsByQuery(query, limit, offset) } returns emptyList()

        // When
        val result = dataSource.getPagedActorsByQuery(query, limit, offset)

        // Then
        assertTrue(result.isEmpty())
        coVerify { actorDao.getPagedActorsByQuery(query, limit, offset) }
    }

    @Test
    fun `getMoviesByQuery_shouldCallMovieDaoWithCorrectPaginationParameters`() = runTest {
        // Given
        val query = "Batman"
        val limit = 20
        val offset = 20
        val expectedMovies = listOf(
            MovieLocalDto(
                1,
                "Batman Begins",
                "img1",
                2005,
                "28,80",
                8.2f,
                "en",
                System.currentTimeMillis()
            ),
            MovieLocalDto(
                2,
                "The Dark Knight",
                "img2",
                2008,
                "28,80,53",
                9.0f,
                "en",
                System.currentTimeMillis()
            )
        )

        coEvery { movieDao.getFilteredMovies(query, limit, offset) } returns expectedMovies

        // When
        val result = dataSource.getMoviesByQuery(query, limit, offset)

        // Then
        assertEquals(expectedMovies, result)
        coVerify { movieDao.getFilteredMovies(query, limit, offset) }
    }

    @Test
    fun `getMoviesByQuery_shouldReturnEmptyList_whenNoResults`() = runTest {
        // Given
        val query = "NonExistentMovie"
        val limit = 20
        val offset = 0

        coEvery { movieDao.getFilteredMovies(query, limit, offset) } returns emptyList()

        // When
        val result = dataSource.getMoviesByQuery(query, limit, offset)

        // Then
        assertTrue(result.isEmpty())
        coVerify { movieDao.getFilteredMovies(query, limit, offset) }
    }

    @Test
    fun `getTvSeriesByQuery_shouldCallSeriesDaoWithCorrectPaginationParameters`() = runTest {
        // Given
        val query = "Breaking Bad"
        val limit = 20
        val offset = 40
        val expectedSeries = listOf(
            TvSeriesLocalDto(
                1,
                "Breaking Bad",
                "img1",
                2008,
                "18,80",
                9.5f,
                "en",
                System.currentTimeMillis()
            ),
            TvSeriesLocalDto(
                2,
                "Better Call Saul",
                "img2",
                2015,
                "18,80",
                8.9f,
                "en",
                System.currentTimeMillis()
            )
        )

        coEvery { seriesDao.getFilteredSeries(query, limit, offset) } returns expectedSeries

        // When
        val result = dataSource.getTvSeriesByQuery(query, limit, offset)

        // Then
        assertEquals(expectedSeries, result)
        coVerify { seriesDao.getFilteredSeries(query, limit, offset) }
    }

    @Test
    fun `getTvSeriesByQuery_shouldReturnEmptyList_whenNoResults`() = runTest {
        // Given
        val query = "NonExistentSeries"
        val limit = 20
        val offset = 0

        coEvery { seriesDao.getFilteredSeries(query, limit, offset) } returns emptyList()

        // When
        val result = dataSource.getTvSeriesByQuery(query, limit, offset)

        // Then
        assertTrue(result.isEmpty())
        coVerify { seriesDao.getFilteredSeries(query, limit, offset) }
    }

    @Test
    fun `pagination_shouldHandleZeroLimit`() = runTest {
        // Given
        val query = "Test"
        val limit = 0
        val offset = 0

        coEvery { actorDao.getPagedActorsByQuery(query, limit, offset) } returns emptyList()
        coEvery { movieDao.getFilteredMovies(query, limit, offset) } returns emptyList()
        coEvery { seriesDao.getFilteredSeries(query, limit, offset) } returns emptyList()

        // When
        val actorsResult = dataSource.getPagedActorsByQuery(query, limit, offset)
        val moviesResult = dataSource.getMoviesByQuery(query, limit, offset)
        val seriesResult = dataSource.getTvSeriesByQuery(query, limit, offset)

        // Then
        assertTrue(actorsResult.isEmpty())
        assertTrue(moviesResult.isEmpty())
        assertTrue(seriesResult.isEmpty())
    }

    @Test
    fun `pagination_shouldHandleNegativeOffset`() = runTest {
        // Given
        val query = "Test"
        val limit = 20
        val offset = -10

        coEvery { actorDao.getPagedActorsByQuery(query, limit, offset) } returns emptyList()

        // When
        val result = dataSource.getPagedActorsByQuery(query, limit, offset)

        // Then
        assertTrue(result.isEmpty())
        coVerify { actorDao.getPagedActorsByQuery(query, limit, offset) }
    }

    @Test
    fun `pagination_shouldHandleVeryLargeLimit`() = runTest {
        // Given
        val query = "Test"
        val limit = 1000
        val offset = 0
        val largeResult = List(1000) { index ->
            ActorLocalDto(index, "Actor $index", "img$index", "en", System.currentTimeMillis())
        }

        coEvery { actorDao.getPagedActorsByQuery(query, limit, offset) } returns largeResult

        // When
        val result = dataSource.getPagedActorsByQuery(query, limit, offset)

        // Then
        assertEquals(1000, result.size)
        coVerify { actorDao.getPagedActorsByQuery(query, limit, offset) }
    }

    @Test
    fun `pagination_shouldHandleConsecutivePages`() = runTest {
        // Given
        val query = "Test"
        val pageSize = 10

        val page1Actors = List(10) { index ->
            ActorLocalDto(index, "Actor $index", "img$index", "en", System.currentTimeMillis())
        }
        val page2Actors = List(10) { index ->
            ActorLocalDto(
                index + 10,
                "Actor ${index + 10}",
                "img${index + 10}",
                "en",
                System.currentTimeMillis()
            )
        }

        coEvery { actorDao.getPagedActorsByQuery(query, pageSize, 0) } returns page1Actors
        coEvery { actorDao.getPagedActorsByQuery(query, pageSize, 10) } returns page2Actors

        // When
        val result1 = dataSource.getPagedActorsByQuery(query, pageSize, 0)
        val result2 = dataSource.getPagedActorsByQuery(query, pageSize, 10)

        // Then
        assertEquals(10, result1.size)
        assertEquals(10, result2.size)
        assertNotEquals(result1, result2)
        assertTrue(result1.all { it.id < 10 })
        assertTrue(result2.all { it.id >= 10 })
    }
}