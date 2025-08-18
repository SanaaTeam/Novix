package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.SearchRemoteDataSource
import com.sanaa.vod.fake.FakeData.MovieSearchResponse
import com.sanaa.vod.fake.FakeData.actorSearchResponse
import com.sanaa.vod.fake.FakeData.tvShowSearchResponse
import com.sanaa.vod.repository.mapper.history.toEntity
import com.sanaa.vod.util.exceptions.ConnectionException
import exceptions.NoNetworkException
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SearchRepositoryImplTest {
    private lateinit var searchRepository: SearchRepositoryImpl
    private val remoteDataSource: SearchRemoteDataSource = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        searchRepository = SearchRepositoryImpl(remoteDataSource)
    }


    @Test
    fun `searchActors() should fetch data from remote when available`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery { remoteDataSource.searchActors(query, page) } returns actorSearchResponse

            val result = searchRepository.searchActors(query, page)

            assertThat(result).isEqualTo(actorSearchResponse.results.map { it.toEntity() })
        }

    @Test
    fun `searchActors() should throw NoNetworkException when a ConnectionException occurs`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery { remoteDataSource.searchActors(any(), any()) } throws ConnectionException()

            assertThrows<NoNetworkException> {
                searchRepository.searchActors(query, page)
            }
        }

    @Test
    fun `searchActors() should throw NovixAppException for a generic exception`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery { remoteDataSource.searchActors(any(), any()) } throws Exception()

            assertThrows<NovixAppException> {
                searchRepository.searchActors(query, page)
            }
        }


    @Test
    fun `searchMovies() should fetch from remote when available`() =
        runTest {
            val page = 1
            val query = "Tom"
            coEvery { remoteDataSource.searchMovies(query, page) } returns MovieSearchResponse

            val result = searchRepository.searchMovies(query, page)

            assertThat(result).isEqualTo(MovieSearchResponse.results.map { it.toEntity() })
        }


    @Test
    fun `searchMovies() should throw NoNetworkException when a ConnectionException occurs`() =
        runTest {
            val page = 1
            val query = "Tom"
            coEvery { remoteDataSource.searchMovies(any(), any()) } throws ConnectionException()

            assertThrows<NoNetworkException> {
                searchRepository.searchMovies(query, page)
            }
        }

    @Test
    fun `searchMovies() should throw NovixAppException for a generic exception`() =
        runTest {
            val page = 1
            val query = "Tom"
            coEvery { remoteDataSource.searchMovies(any(), any()) } throws Exception()

            assertThrows<NovixAppException> {
                searchRepository.searchMovies(query, page)
            }
        }

    @Test
    fun `searchTvShows() should fetch from remote when available`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery { remoteDataSource.searchTvShows(query, page) } returns tvShowSearchResponse

            val result = searchRepository.searchTvShows(query, page)

            assertThat(result).isEqualTo(tvShowSearchResponse.results.map { it.toEntity() })
        }

    @Test
    fun `searchTvShows() should throw NoNetworkException when a ConnectionException occurs`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery { remoteDataSource.searchTvShows(any(), any()) } throws ConnectionException()

            assertThrows<NoNetworkException> {
                searchRepository.searchTvShows(query, page)
            }
        }

    @Test
    fun `searchTvShows should throw NovixAppException for a generic exception`() =
        runTest {
            val page = 1
            val query = "Jane"
            coEvery { remoteDataSource.searchTvShows(any(), any()) } throws Exception()

            assertThrows<NovixAppException> {
                searchRepository.searchTvShows(query, page)
            }
        }
}