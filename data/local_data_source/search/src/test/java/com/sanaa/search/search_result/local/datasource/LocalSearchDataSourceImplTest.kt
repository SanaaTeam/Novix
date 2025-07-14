package com.sanaa.search.search_result.local.datasource

import com.example.env_config.service.LanguageProvider
import com.sanaa.search.dataSource.local.dto.*
import com.sanaa.search.search_result.LocalCachedSearchDataSourceImpl
import com.sanaa.search.search_result.dao.ActorDao
import com.sanaa.search.search_result.dao.MovieDao
import com.sanaa.search.search_result.dao.SearchDao
import com.sanaa.search.search_result.dao.SearchResultDao
import com.sanaa.search.search_result.dao.SeriesDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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
        val insertedId = 1L

        coEvery { searchDao.getSearchByQueryAndLanguage(query, any()) } returns null
        coEvery { searchDao.insertSearch(any()) } returns insertedId
        coEvery { searchDao.updateTimestamp(any(), any(), any()) } returns Unit
        coEvery { searchResultDao.insert(any()) } returns Unit

        dataSource.cacheSearchResult(query, itemId, itemType)

        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 1) { searchDao.insertSearch(match { it.query == query && it.language == "en" }) }
        coVerify(exactly = 1) { searchResultDao.insert(match { it.id == insertedId.toInt() && it.itemId == itemId && it.itemType == itemType }) }
    }

    @Test
    fun `cacheSearchResult should update timestamp when existing search exists`() = runTest {
        val query = "existing query"
        val itemId = 123
        val itemType = "movie"
        val existingSearch = SearchLocalDto(
            id = 42,
            query = query,
            language = "en",
            timestamp = System.currentTimeMillis()
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

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns SearchLocalDto(
            1,
            query,
            "en",
            System.currentTimeMillis()
        )
        coEvery { searchResultDao.getByQueryAndLanguage(query, "en", type) } returns expected

        val result = dataSource.getCachedResults(query, type)

        assertEquals(expected, result)
        coVerify { searchResultDao.getByQueryAndLanguage(query, "en", type) }
    }

    @Test
    fun `getCachedResults returns empty list when search is expired`() = runTest {
        val query = "expired query"
        val expiredTimestamp = System.currentTimeMillis() - (LocalCachedSearchDataSourceImpl.CACHE_EXPIRATION_TIME + 1000)
        val expiredSearch = SearchLocalDto(
            id = 1,
            query = query,
            language = "en",
            timestamp = expiredTimestamp
        )

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns expiredSearch

        val result = dataSource.getCachedResults(query, "movie")

        assertEquals(emptyList<SearchResultLocalDto>(), result)
        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 0) { searchDao.updateTimestamp(any(), any(), any()) }
    }

    @Test
    fun `getCachedResults returns empty list when search is null`() = runTest {
        val query = "no search"

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null

        val result = dataSource.getCachedResults(query, "movie")

        assertEquals(emptyList<SearchResultLocalDto>(), result)
        coVerify(exactly = 1) { searchDao.getSearchByQueryAndLanguage(query, "en") }
        coVerify(exactly = 0) { searchDao.updateTimestamp(any(), any(), any()) }
    }

    @Test
    fun `clearExpiredCache should call deleteOldResults`() = runTest {
        val expiredTime = System.currentTimeMillis() - 3600000L

        dataSource.clearExpiredCache(expiredTime)

        coVerify { searchResultDao.deleteOldResults(expiredTime) }
    }

    @Test
    fun `getActorsByQuery returns cached results`() = runTest {
        val query = "actor query"
        val cachedResults = listOf(SearchResultLocalDto(1, 123, "actor"))
        val actor = ActorsLocalDto(123, "Actor Name", "", "en", System.currentTimeMillis())

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns SearchLocalDto(1, query, "en", System.currentTimeMillis())
        coEvery { searchResultDao.getByQueryAndLanguage(query, "en", "actor") } returns cachedResults
        coEvery { actorDao.getActorsByQuery("123") } returns listOf(actor)

        val result = dataSource.getActorsByQuery(query)

        assertEquals(listOf(actor), result)
        coVerify { actorDao.getActorsByQuery("123") }
    }

    @Test
    fun `getActorsByQuery returns direct actorDao result if no cached results`() = runTest {
        val query = "actor query"

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null
        coEvery { actorDao.getActorsByQuery(query) } returns listOf(ActorsLocalDto(
            id = 99, name = "Actor", imagePath = "", language = "en", timestamp = System.currentTimeMillis()
        ))

        val result = dataSource.getActorsByQuery(query)

        assertEquals(1, result.size)
        coVerify(exactly = 1) { actorDao.getActorsByQuery(query) }
    }

    @Test
    fun `getMoviesByQuery returns cached results`() = runTest {
        val query = "movie query"
        val cachedResults = listOf(SearchResultLocalDto(1, 123, "movie"))
        val movies = listOf(MoviesLocalDto(123, "Movie1", "", 2020, null, 7.5f, "en", System.currentTimeMillis()))

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns SearchLocalDto(1, query, "en", System.currentTimeMillis())
        coEvery { searchResultDao.getByQueryAndLanguage(query, "en", "movie") } returns cachedResults
        coEvery { movieDao.getFilteredMovies("123") } returns movies

        val result = dataSource.getMoviesByQuery(query)

        assertEquals(movies, result)
    }

    @Test
    fun `getMoviesByQuery returns direct movieDao results if no cached results`() = runTest {
        val query = "movie query"
        val movieList = listOf(
            MoviesLocalDto(1, "Movie1", "", 2020, null, 7.5f, "en", System.currentTimeMillis())
        )

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null
        coEvery { movieDao.getFilteredMovies(query) } returns movieList

        val result = dataSource.getMoviesByQuery(query)

        assertEquals(movieList, result)
        coVerify(exactly = 1) { movieDao.getFilteredMovies(query) }
    }

    @Test
    fun `getTvSeriesByQuery returns cached results`() = runTest {
        val query = "tv query"
        val cachedResults = listOf(SearchResultLocalDto(1, 123, "tv_series"))
        val series = listOf(TvSeriesLocalDto(123, "Series1", "", 2020, null, 8.1f, "en", System.currentTimeMillis()))

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns SearchLocalDto(1, query, "en", System.currentTimeMillis())
        coEvery { searchResultDao.getByQueryAndLanguage(query, "en", "tv_series") } returns cachedResults
        coEvery { seriesDao.getFilteredSeries("123") } returns series

        val result = dataSource.getTvSeriesByQuery(query)

        assertEquals(series, result)
    }

    @Test
    fun `getTvSeriesByQuery returns empty list if no cached results`() = runTest {
        val query = "tv query"

        coEvery { searchDao.getSearchByQueryAndLanguage(query, "en") } returns null

        val result = dataSource.getTvSeriesByQuery(query)

        assertEquals(emptyList<TvSeriesLocalDto>(), result)
    }

    @Test
    fun `insertMovie should call insertMovie from movieDao`() = runTest {
        val movie = MoviesLocalDto(
            id = 1,
            title = "title",
            imagePath = "",
            releaseYear = 10,
            genres = null,
            imdbRating = 2.1f,
            language = "en",
            timestamp = System.currentTimeMillis(),
        )
        coEvery { movieDao.insertMovie(movie) } returns Unit

        dataSource.cacheMovie(movie)

        coVerify { movieDao.insertMovie(movie) }
    }

    @Test
    fun `insertActor should call insertActor from actorDao`() = runTest {
        val actor = ActorsLocalDto(
            id = 1,
            name = "name",
            imagePath = "",
            language = "en",
            timestamp = System.currentTimeMillis(),
        )
        coEvery { actorDao.insertActor(actor) } returns Unit

        dataSource.cacheActor(actor)

        coVerify { actorDao.insertActor(actor) }
    }

    @Test
    fun `insertSeries should call insertSeries from seriesDao`() = runTest {
        val series = TvSeriesLocalDto(
            id = 1,
            title = "title",
            imagePath = "",
            releaseYear = 10,
            genres = null,
            imdbRating = 2.1f,
            language = "en",
            timestamp = System.currentTimeMillis(),
        )
        coEvery { seriesDao.insertSeries(series) } returns Unit

        dataSource.cacheTvSeries(series)

        coVerify { seriesDao.insertSeries(series) }
    }
}