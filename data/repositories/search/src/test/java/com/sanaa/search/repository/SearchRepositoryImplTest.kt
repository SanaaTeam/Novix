package com.sanaa.search.repository

import com.example.env_config.service.LanguageProvider
import com.google.common.truth.Truth.assertThat
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import com.sanaa.search.dataSource.remote.response.SearchResponse
import com.sanaa.search.mapper.toSearchOutput
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import search.usecase.search_param.MediaFilters
import java.nio.channels.UnresolvedAddressException

class SearchRepositoryImplTest {
    private lateinit var searchRepository: SearchRepositoryImpl
    private val remoteDataSource: SearchRemoteDataSource = mockk(relaxed = true)
    private val localCacheSearchDataSource: LocalCacheSearchDataSource = mockk(relaxed = true)
    private val languageProvider: LanguageProvider = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        searchRepository =
            SearchRepositoryImpl(remoteDataSource, localCacheSearchDataSource, languageProvider)
    }

    @Test
    fun `searchActors should returns cached actors when cashed data available`() = runTest {
        // Given
        val query = "Tom"
        coEvery { localCacheSearchDataSource.getActorsByQuery(query) } returns ActorsLocalDtoList

        // When
        val expected = ActorsLocalDtoList.map { it.toSearchOutput() }
        val result = searchRepository.searchActors(query)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `searchActors should fetches remote and caches when there is no cached data`() = runTest {
        val query = "Jane"
        coEvery { localCacheSearchDataSource.getActorsByQuery(query) } returns emptyList()
        coEvery { remoteDataSource.searchActors(query) } returns actorSearchResponse
        coEvery { localCacheSearchDataSource.cacheActor(any()) } just Runs

        searchRepository.searchActors(query)

        coVerify { remoteDataSource.searchActors(query) }
    }

    @Test
    fun `searchActors throws NoNetworkException when throw UnresolvedAddressException `() =
        runTest {
            coEvery { localCacheSearchDataSource.getActorsByQuery(any()) } throws UnresolvedAddressException()
            assertThrows<NoNetworkException> {
                searchRepository.searchActors("x")
            }
        }

    @Test
    fun `searchActors should throw RetrievingDataFailureException when throw an exception`() =
        runTest {
            coEvery { localCacheSearchDataSource.getActorsByQuery(any()) } throws Exception()
            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchActors("x")
            }
        }

    @Test
    fun `searchMovies should returns cached movies when cashed data available`() = runTest {
        // Given
        val query = "Tom"
        val filters = MediaFilters()
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query) } returns MoviesLocalDtoList

        // When
        val expected = MoviesLocalDtoList.map { it.toSearchOutput() }
        val result = searchRepository.searchMovies(query, filters)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `searchMovies should fetches remote and caches when there is no cached data`() = runTest {
        val query = "Jane"
        val filters = MediaFilters()
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query) } returns emptyList()
        coEvery { remoteDataSource.searchMovies(query) } returns MovieSearchResponse
        coEvery { localCacheSearchDataSource.cacheMovie(any()) } just Runs

        searchRepository.searchMovies(query, filters)

        coVerify { remoteDataSource.searchMovies(query) }
    }

    @Test
    fun `searchMovies throws NoNetworkException when throw UnresolvedAddressException `() =
        runTest {
            val filters = MediaFilters()
            coEvery { localCacheSearchDataSource.getMoviesByQuery(any()) } throws UnresolvedAddressException()
            assertThrows<NoNetworkException> {
                searchRepository.searchMovies("x", filters)
            }
        }

    @Test
    fun `searchMovies should throw RetrievingDataFailureException when throw an exception`() =
        runTest {
            val filters = MediaFilters()
            coEvery { localCacheSearchDataSource.getMoviesByQuery(any()) } throws Exception()
            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchMovies("x", filters)
            }
        }

    @Test
    fun `searchTvShows should returns cached tv shows when cashed data available`() = runTest {
        // Given
        val query = "Tom"
        val filters = MediaFilters()
        coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query) } returns TvSeriesLocalDtoList

        // When
        val expected = TvSeriesLocalDtoList.map { it.toSearchOutput() }
        val result = searchRepository.searchTvShows(query, filters)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `searchTvShows should fetches remote and caches when there is no cached data`() = runTest {
        val query = "Jane"
        val filters = MediaFilters()
        coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query) } returns emptyList()
        coEvery { remoteDataSource.searchTvShows(query) } returns TvSeriesSearchResponse
        coEvery { localCacheSearchDataSource.cacheTvSeries(any()) } just Runs

        searchRepository.searchTvShows(query, filters)

        coVerify { remoteDataSource.searchTvShows(query) }
    }

    @Test
    fun `searchTvShows throws NoNetworkException when throw UnresolvedAddressException `() =
        runTest {
            val query = "Jane"
            val filters = MediaFilters()
            coEvery { localCacheSearchDataSource.getTvSeriesByQuery(any()) } throws UnresolvedAddressException()
            assertThrows<NoNetworkException> {
                searchRepository.searchTvShows(query, filters)
            }
        }

    @Test
    fun `searchTvShows should throw RetrievingDataFailureException when throw an exception`() =
        runTest {
            val filters = MediaFilters()
            coEvery { localCacheSearchDataSource.getTvSeriesByQuery(any()) } throws Exception()
            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchTvShows("x", filters)
            }
        }

    companion object {
        private val ActorsLocalDtoList = listOf(
            ActorsLocalDto(id = 1, name = "Tom Hanks", imagePath = "img", language = "en"),
            ActorsLocalDto(id = 2, name = "Leonardo DiCaprio", imagePath = "img", language = "en")
        )
        private val ActorsRemoteDtoList = listOf(
            ActorSearchDto(
                id = 1,
                name = "Tom Hanks",
                profileImagePath = "/path/to/image"
            ),
            ActorSearchDto(
                id = 2,
                name = "sam",
                profileImagePath = "/path/to/image"
            ),
        )
        private val actorSearchResponse = SearchResponse(
            page = 1,
            results = ActorsRemoteDtoList,
            totalPages = 1,
            totalResults = ActorsRemoteDtoList.size
        )

        private val MoviesLocalDtoList = listOf(
            MoviesLocalDto(
                id = 1, "Movie", "2025", null,
                null, null, "en", System.currentTimeMillis()
            ),
            MoviesLocalDto(
                id = 1, "Movie2", "2025", null,
                null, null, "en", System.currentTimeMillis()
            ),
        )

        private val MoviesRemoteDtoList = listOf(
            MovieSearchDto(
                id = 1, "Movie", "2025", null, null, null,
            ),
            MovieSearchDto(
                id = 1, "Movie2", "2025", null, null, null
            ),
        )

        private val MovieSearchResponse = SearchResponse(
            page = 1,
            results = MoviesRemoteDtoList,
            totalPages = 1,
            totalResults = MoviesRemoteDtoList.size
        )

        private val TvSeriesLocalDtoList = listOf(
            TvSeriesLocalDto(
                id = 1, "TvSeries", "2025", null,
                null, null, "en", System.currentTimeMillis()
            ),
            TvSeriesLocalDto(
                id = 1, "TvSeries2", "2025", null,
                null, null, "en", System.currentTimeMillis()
            ),
        )

        private val TvSeriesRemoteDtoList = listOf(
            TvShowSearchDto(
                id = 1, "TvSeries", "2025", null, null, null,
            ),
            TvShowSearchDto(
                id = 1, "TvSeries2", "2025", null, null, null
            ),
        )

        private val TvSeriesSearchResponse = SearchResponse(
            page = 1,
            results = TvSeriesRemoteDtoList,
            totalPages = 1,
            totalResults = TvSeriesRemoteDtoList.size
        )
    }
}