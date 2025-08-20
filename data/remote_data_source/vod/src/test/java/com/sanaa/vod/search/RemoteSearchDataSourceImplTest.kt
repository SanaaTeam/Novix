package com.sanaa.vod.search

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.SearchRemoteDataSource
import com.sanaa.vod.dataSource.remote.dto.search.ActorSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.MovieSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.response.SearchResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoteSearchDataSourceImplTest {
    private lateinit var dataSource: SearchRemoteDataSource
    private val apiService = mockk<SearchApiService>()

    @BeforeEach
    fun setUp() {
        dataSource = SearchRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `searchActors should return SearchResponse of ActorSearchDto`() = runTest {
        coEvery { apiService.searchActors("Brad", 1) } returns dummyActorSearchResponse

        val result = dataSource.searchActors("Brad", 1)

        assertThat(result.results.first().id).isEqualTo(dummyActorSearchResponse.results.first().id)
    }

    @Test
    fun `searchTvShows should return SearchResponse of TvShowSearchDto`() = runTest {
        coEvery { apiService.searchTvShows("Breaking", 1) } returns dummyTvShowSearchResponse

        val result = dataSource.searchTvShows("Breaking", 1)

        assertThat(result.results.first().id).isEqualTo(dummyTvShowSearchResponse.results.first().id)
    }

    @Test
    fun `searchMovies should return SearchResponse of MovieSearchDto`() = runTest {
        coEvery { apiService.searchMovies("Inception", 1) } returns dummyMovieSearchResponse

        val result = dataSource.searchMovies("Inception", 1)

        assertThat(result.results.first().id).isEqualTo(dummyMovieSearchResponse.results.first().id)
    }

    private companion object {
        val dummyActorSearchResponse = SearchResponse(
            page = 1,
            results = listOf(
                ActorSearchDto(
                    id = 1,
                    name = "Brad Pitt",
                    profileImagePath = "/path/to/profile.jpg",
                    gender = 1,
                    knownForDepartment = "Acting"
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        val dummyTvShowSearchResponse = SearchResponse(
            page = 1,
            results = listOf(
                TvShowSearchDto(
                    id = 1,
                    name = "Breaking Bad",
                    posterImagePath = "/path/to/poster.jpg",
                    releaseDate = "2008-01-20",
                    voteAverage = 9.5f,
                    genreIds = listOf(18, 80)
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        val dummyMovieSearchResponse = SearchResponse(
            page = 1,
            results = listOf(
                MovieSearchDto(
                    id = 1,
                    title = "Inception",
                    posterImagePath = "/path/to/poster.jpg",
                    releaseDate = "2010-07-16",
                    voteAverage = 8f,
                    genreIds = listOf(28, 878, 12)
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
    }
}