package com.sanaa.search.repository

import com.example.env_config.service.LanguageProvider
import com.google.common.truth.Truth.assertThat
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.fake.FakeData.ActorsLocalDtoList
import com.sanaa.search.fake.FakeData.MovieSearchResponse
import com.sanaa.search.fake.FakeData.MoviesLocalDtoList
import com.sanaa.search.fake.FakeData.TvSeriesLocalDtoList
import com.sanaa.search.fake.FakeData.TvSeriesSearchResponse
import com.sanaa.search.fake.FakeData.actorSearchResponse
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
    fun `searchActors() should returns cached actors when cashed data available`() = runTest {
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
    fun `searchActors() should fetches remote and caches when there is there is no cached data`() =
        runTest {
            val query = "Jane"
            coEvery { localCacheSearchDataSource.getActorsByQuery(query) } returns emptyList()
            coEvery { remoteDataSource.searchActors(query) } returns actorSearchResponse
            coEvery { localCacheSearchDataSource.cacheActor(any()) } just Runs

            searchRepository.searchActors(query)

            coVerify { remoteDataSource.searchActors(query) }
        }

    @Test
    fun `searchActors() throws NoNetworkException when throw UnresolvedAddressException`() =
        runTest {
            val query = "Jane"
            coEvery { localCacheSearchDataSource.getActorsByQuery(any()) } throws UnresolvedAddressException()
            assertThrows<NoNetworkException> {
                searchRepository.searchActors(query)
            }
        }

    @Test
    fun `searchActors() should throw RetrievingDataFailureException when throw an exception`() =
        runTest {
            val query = "Jane"
            coEvery { localCacheSearchDataSource.getActorsByQuery(any()) } throws Exception()
            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchActors(query)
            }
        }

    @Test
    fun `searchMovies() should returns cached movies when cashed data available`() = runTest {
        // Given
        val query = "Tom"
        val filters = MediaFilters(startYear = 2025, endYear = 2025)
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query) } returns MoviesLocalDtoList

        // When
        val expected =
            MoviesLocalDtoList.filter { it.releaseYear == 2025 }.map { it.toSearchOutput() }
        val result = searchRepository.searchMovies(query, filters)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `searchMovies() should fetches remote and caches movies when there is no cached data`() =
        runTest {
            // Given
            val query = "Tom"
            coEvery { localCacheSearchDataSource.getMoviesByQuery(query) } returns MoviesLocalDtoList

            // When
            val expected = MoviesLocalDtoList.map { it.toSearchOutput() }
            val result = searchRepository.searchMovies(query, null)

            // Then
            assertThat(result).isEqualTo(expected)
        }

    @Test
    fun `searchMovies() should returns filtered cached movies when cashed data available and chose passing filters`() =
        runTest {
            val query = "Tom"
            val filters = MediaFilters(startYear = 2025, endYear = 2025)
            coEvery { localCacheSearchDataSource.getMoviesByQuery(query) } returns emptyList()
            coEvery { remoteDataSource.searchMovies(query) } returns MovieSearchResponse
            coEvery { localCacheSearchDataSource.cacheMovie(any()) } just Runs

            val expected = MoviesLocalDtoList
                .filter { it.releaseYear == 2025 }.map { it.toSearchOutput() }
            val result = searchRepository.searchMovies(query, filters)
            assertThat(result).isEqualTo(expected)
        }

    @Test
    fun `searchMovies() should fetches remote and cache filtered movies when there is no cached data and choose filters`() =
        runTest {
            val query = "Tom"
            coEvery { localCacheSearchDataSource.getMoviesByQuery(query) } returns emptyList()
            coEvery { remoteDataSource.searchMovies(query) } returns MovieSearchResponse
            coEvery { localCacheSearchDataSource.cacheMovie(any()) } just Runs

            val expected = MoviesLocalDtoList.map { it.toSearchOutput() }
            val result = searchRepository.searchMovies(query, null)
            assertThat(result).isEqualTo(expected)
        }

    @Test
    fun `searchMovies() should fetches remote and caches movies without filtration when there is no cached data and not choose filters`() =
        runTest {
            val query = "Tom"
            coEvery { localCacheSearchDataSource.getMoviesByQuery(query) } returns emptyList()
            coEvery { remoteDataSource.searchMovies(query) } returns MovieSearchResponse
            coEvery { localCacheSearchDataSource.cacheMovie(any()) } just Runs

            searchRepository.searchMovies(query, null)

            coVerify { remoteDataSource.searchMovies(query) }
        }

    @Test
    fun `searchMovies() should throws NoNetworkException when throw UnresolvedAddressException`() =
        runTest {
            val query = "Tom"
            val filters = MediaFilters()
            coEvery { localCacheSearchDataSource.getMoviesByQuery(any()) } throws UnresolvedAddressException()
            assertThrows<NoNetworkException> {
                searchRepository.searchMovies(query, filters)
            }
        }

    @Test
    fun `searchMovies() should throw RetrievingDataFailureException when throw an exception`() =
        runTest {
            val query = "Tom"
            val filters = MediaFilters()
            coEvery { localCacheSearchDataSource.getMoviesByQuery(any()) } throws Exception()
            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchMovies(query, filters)
            }
        }

    @Test
    fun `searchTvShows() should returns cached filtered tv shows when cashed data available and choose filters`() =
        runTest {
            // Given
            val query = "Tom"
            val filters = MediaFilters(startYear = 2025, endYear = 2025)
            coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query) } returns TvSeriesLocalDtoList

            // When
            val expected =
                TvSeriesLocalDtoList.filter { it.releaseYear == 2025 }.map { it.toSearchOutput() }
            val result = searchRepository.searchTvShows(query, filters)

            // Then
            assertThat(result).isEqualTo(expected)
        }


    @Test
    fun `searchTvShows() should returns cached tv shows without filtration when cashed data available and not choose filters`() =
        runTest {
            // Given
            val query = "Tom"
            coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query) } returns TvSeriesLocalDtoList

            // When
            val expected = TvSeriesLocalDtoList.map { it.toSearchOutput() }
            val result = searchRepository.searchTvShows(query, null)

            // Then
            assertThat(result).isEqualTo(expected)
        }


    @Test
    fun `searchTvShows() should fetches remote and caches tv shows when there is no cached data`() =
        runTest {
            val query = "Jane"
            val filters = MediaFilters()
            coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query) } returns emptyList()
            coEvery { remoteDataSource.searchTvShows(query) } returns TvSeriesSearchResponse
            coEvery { localCacheSearchDataSource.cacheTvSeries(any()) } just Runs

            searchRepository.searchTvShows(query, filters)

            coVerify { remoteDataSource.searchTvShows(query) }
        }


    @Test
    fun `searchTvShows() should returns filtered cached tv shows when cashed data available and chose passing filters`() =
        runTest {
            val query = "Tom"
            val filters = MediaFilters(startYear = 2025, endYear = 2025)
            coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query) } returns emptyList()
            coEvery { remoteDataSource.searchTvShows(query) } returns TvSeriesSearchResponse
            coEvery { localCacheSearchDataSource.cacheTvSeries(any()) } just Runs

            val expected =
                MoviesLocalDtoList.filter { it.releaseYear == 2025 }.map { it.toSearchOutput() }
            val result = searchRepository.searchTvShows(query, filters)
            assertThat(result).isEqualTo(expected)
        }

    @Test
    fun `searchTvShows() should fetches remote and caches tv shows without filtration when there is no cached data and not choose filters`() =
        runTest {
            val query = "Tom"
            coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query) } returns emptyList()
            coEvery { remoteDataSource.searchTvShows(query) } returns TvSeriesSearchResponse
            coEvery { localCacheSearchDataSource.cacheTvSeries(any()) } just Runs

            val expected = TvSeriesLocalDtoList.map { it.toSearchOutput() }
            val result = searchRepository.searchTvShows(query, null)
            assertThat(result).isEqualTo(expected)
        }

    @Test
    fun `searchTvShows() should throws NoNetworkException when throw UnresolvedAddressException`() =
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
            val query = "Jane"
            val filters = MediaFilters()
            coEvery { localCacheSearchDataSource.getTvSeriesByQuery(any()) } throws Exception()
            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchTvShows(query, filters)
            }
        }
}