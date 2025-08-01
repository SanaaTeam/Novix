package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.dataSource.remote.search.search.SearchRemoteDataSource
import com.sanaa.vod.fake.FakeData.ActorsLocalDtoList
import com.sanaa.vod.fake.FakeData.MovieSearchResponse
import com.sanaa.vod.fake.FakeData.MoviesLocalDtoList
import com.sanaa.vod.fake.FakeData.TvSeriesLocalDtoList
import com.sanaa.vod.fake.FakeData.TvSeriesSearchResponse
import com.sanaa.vod.fake.FakeData.actorSearchResponse
import com.sanaa.vod.mapper.search.toEntity
import com.sanaa.vod.util.exceptions.ConnectionException
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

class SearchRepositoryImplTest {
    private lateinit var searchRepository: SearchRepositoryImpl
    private val remoteDataSource: SearchRemoteDataSource = mockk(relaxed = true)
    private val localCacheSearchDataSource: LocalCacheSearchDataSource = mockk(relaxed = true)
    private val languageProvider: LanguageProvider = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        searchRepository = SearchRepositoryImpl(
            remoteDataSource, localCacheSearchDataSource, languageProvider
        )
    }

    @Test
    fun `searchActors() should return cached actors when cached data is available`() = runTest {
        // Given
        val page = 1
        val query = "Tom"
        coEvery {
            localCacheSearchDataSource.getPagedActorsByQuery(query, any(), any())
        } returns ActorsLocalDtoList

        // When
        val expected = ActorsLocalDtoList.map { it.toEntity() }
        val result = searchRepository.searchActors(query, page)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `searchActors() should fetch from remote and cache when there is no cached data`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery {
                localCacheSearchDataSource.getPagedActorsByQuery(query, any(), any())
            } returns emptyList()
            coEvery { remoteDataSource.searchActors(query, page) } returns actorSearchResponse
            coEvery { localCacheSearchDataSource.cacheActor(any()) } just Runs

            searchRepository.searchActors(query, page)

            coVerify { remoteDataSource.searchActors(query, page) }
        }

    @Test
    fun `searchActors() throws NoNetworkException when a ConnectionException occurs`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery {
                localCacheSearchDataSource.getPagedActorsByQuery(query, any(), any())
            } throws ConnectionException()
            assertThrows<NoNetworkException> {
                searchRepository.searchActors(query, page)
            }
        }

    @Test
    fun `searchActors() should throw RetrievingDataFailureException for a generic exception`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery {
                localCacheSearchDataSource.getPagedActorsByQuery(query, any(), any())
            } throws Exception()
            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchActors(query, page)
            }
        }

    @Test
    fun `searchMovies() should return cached movies when cached data is available`() = runTest {
        // Given
        val page = 1
        val query = "Tom"
        coEvery {
            localCacheSearchDataSource.getMoviesByQuery(query, any(), any())
        } returns MoviesLocalDtoList

        // When
        val expected = MoviesLocalDtoList.map { it.toEntity() }
        val result = searchRepository.searchMovies(query, page)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `searchMovies() should fetch from remote when cache is empty`() =
        runTest {
            val page = 1
            val query = "Tom"
            coEvery {
                localCacheSearchDataSource.getMoviesByQuery(query, any(), any())
            } returns emptyList()
            coEvery { remoteDataSource.searchMovies(query, page) } returns MovieSearchResponse
            coEvery { localCacheSearchDataSource.cacheMovie(any()) } just Runs

            searchRepository.searchMovies(query, page)

            coVerify { remoteDataSource.searchMovies(query, page) }
        }


    @Test
    fun `searchMovies() should throw NoNetworkException when a ConnectionException occurs`() =
        runTest {
            val page = 1
            val query = "Tom"
            coEvery {
                localCacheSearchDataSource.getMoviesByQuery(any(), any(), any())
            } throws ConnectionException()
            assertThrows<NoNetworkException> {
                searchRepository.searchMovies(query, page)
            }
        }

    @Test
    fun `searchMovies() should throw RetrievingDataFailureException for a generic exception`() =
        runTest {
            val page = 1
            val query = "Tom"
            coEvery {
                localCacheSearchDataSource.getMoviesByQuery(query, any(), any())
            } throws Exception()

            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchMovies(query, page)
            }
        }

    @Test
    fun `searchTvShows() should return cached TV shows when cached data is available`() =
        runTest {
            // Given
            val page = 1
            val query = "Tom"
            coEvery {
                localCacheSearchDataSource.getTvSeriesByQuery(query, any(), any())
            } returns TvSeriesLocalDtoList

            // When
            val expected = TvSeriesLocalDtoList.map { it.toEntity() }
            val result = searchRepository.searchTvShows(query, page)

            // Then
            assertThat(result).isEqualTo(expected)
        }


    @Test
    fun `searchTvShows() should fetch from remote when cache is empty`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery {
                localCacheSearchDataSource.getTvSeriesByQuery(query, any(), any())
            } returns emptyList()
            coEvery { remoteDataSource.searchTvShows(query, page) } returns TvSeriesSearchResponse
            coEvery { localCacheSearchDataSource.cacheTvSeries(any()) } just Runs

            searchRepository.searchTvShows(query, page)

            coVerify { remoteDataSource.searchTvShows(query, page) }
        }

    @Test
    fun `searchTvShows() should throw NoNetworkException when a ConnectionException occurs`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery {
                localCacheSearchDataSource.getTvSeriesByQuery(query, any(), any())
            } throws ConnectionException()
            assertThrows<NoNetworkException> {
                searchRepository.searchTvShows(query, page)
            }
        }

    @Test
    fun `searchTvShows should throw RetrievingDataFailureException for a generic exception`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery {
                localCacheSearchDataSource.getTvSeriesByQuery(query, any(), any())
            } throws Exception()
            assertThrows<RetrievingDataFailureException> {
                searchRepository.searchTvShows(query, page)
            }
        }
}