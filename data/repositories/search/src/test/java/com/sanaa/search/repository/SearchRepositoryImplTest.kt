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
import com.sanaa.search.mapper.toSearchOutput
import com.sanaa.search.dataSource.remote.response.SearchResponse
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
import search.usecase.search_param.MediaType
import java.net.UnknownHostException

class SearchRepositoryImplTest {

    private lateinit var searchRepository: SearchRepositoryImpl
    private val remoteDataSource: SearchRemoteDataSource = mockk(relaxed = true)
    private val localCacheSearchDataSource: LocalCacheSearchDataSource = mockk(relaxed = true)
    private val languageProvider: LanguageProvider = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        coEvery { languageProvider.getCurrentLanguage() } returns "en"
        searchRepository =
            SearchRepositoryImpl(remoteDataSource, localCacheSearchDataSource, languageProvider)
    }

    @Test
    fun `searchActors returns cached actors when available`() = runTest {
        // Given
        val query = "Tom"
        val page = 1
        coEvery { localCacheSearchDataSource.getPagedActorsByQuery(query, 20, 0) } returns ActorsLocalDtoList

        // When
        val expected = ActorsLocalDtoList.map { it.toSearchOutput() }
        val result = searchRepository.searchActors(query, page)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `searchActors fetches remote and caches when no local`() = runTest {
        val query = "Jane"
        val page = 1
        coEvery { localCacheSearchDataSource.getPagedActorsByQuery(query, 20, 0) } returns emptyList()
        coEvery { remoteDataSource.searchActors(query, page) } returns actorSearchResponse
        coEvery { localCacheSearchDataSource.cacheActor(any()) } just Runs

        searchRepository.searchActors(query, page)

        coVerify { remoteDataSource.searchActors(query, page) }
        coVerify(exactly = 2) { localCacheSearchDataSource.cacheActor(any()) }
    }

    @Test
    fun `searchActors throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { localCacheSearchDataSource.getPagedActorsByQuery(any(), any(), any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> {
            searchRepository.searchActors("x", 1)
        }
    }

    @Test
    fun `searchActors transforms other exceptions`() = runTest {
        coEvery { localCacheSearchDataSource.getPagedActorsByQuery(any(), any(), any()) } throws Exception("oops")
        assertThrows<RetrievingDataFailureException> {
            searchRepository.searchActors("x", 1)
        }
    }

    @Test
    fun `searchMovies with filters applies filters correctly to local data`() = runTest {
        // Given
        val query = "Batman"
        val page = 1
        val filters = MediaFilters(
            genres = listOf(createGenre(28)), // Action
            imdbRating = 7.0f,
            startYear = 2020,
            endYear = 2024
        )
        
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query, 20, 0) } returns MoviesLocalDtoList

        // When
        val result = searchRepository.searchMedia(query, page, filters, MediaType.MOVIE)

        // Then
        assertThat(result).isNotEmpty()
        // Should filter out movies that don't meet criteria
        assertThat(result.size).isLessThan(MoviesLocalDtoList.size)
    }

    @Test
    fun `searchMovies without filters returns all cached movies`() = runTest {
        // Given
        val query = "Batman"
        val page = 1
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query, 20, 0) } returns MoviesLocalDtoList

        // When
        val result = searchRepository.searchMedia(query, page, null, MediaType.MOVIE)

        // Then
        assertThat(result).hasSize(MoviesLocalDtoList.size)
    }

    @Test
    fun `searchMovies fetches remote when no local data`() = runTest {
        // Given
        val query = "Batman"
        val page = 1
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query, 20, 0) } returns emptyList()
        coEvery { remoteDataSource.searchMovies(query, page) } returns movieSearchResponse
        coEvery { localCacheSearchDataSource.cacheMovie(any()) } just Runs

        // When
        searchRepository.searchMedia(query, page, null, MediaType.MOVIE)

        // Then
        coVerify { remoteDataSource.searchMovies(query, page) }
        coVerify(exactly = 2) { localCacheSearchDataSource.cacheMovie(any()) }
    }

    @Test
    fun `searchTvSeries with filters applies filters correctly`() = runTest {
        // Given
        val query = "Breaking Bad"
        val page = 1
        val filters = MediaFilters(
            genres = listOf(createGenre(18)), // Drama
            imdbRating = 8.0f,
            startYear = 2008,
            endYear = 2013
        )
        
        coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query, 20, 0) } returns TvSeriesLocalDtoList

        // When
        val result = searchRepository.searchMedia(query, page, filters, MediaType.TV_SERIES)

        // Then
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `searchTvSeries fetches remote when no local data`() = runTest {
        // Given
        val query = "Breaking Bad"
        val page = 1
        coEvery { localCacheSearchDataSource.getTvSeriesByQuery(query, 20, 0) } returns emptyList()
        coEvery { remoteDataSource.searchTv(query, page) } returns tvSeriesSearchResponse
        coEvery { localCacheSearchDataSource.cacheTvSeries(any()) } just Runs

        // When
        searchRepository.searchMedia(query, page, null, MediaType.TV_SERIES)

        // Then
        coVerify { remoteDataSource.searchTv(query, page) }
        coVerify(exactly = 2) { localCacheSearchDataSource.cacheTvSeries(any()) }
    }

    @Test
    fun `pagination works correctly with different pages`() = runTest {
        // Given
        val query = "Batman"
        val page1 = 1
        val page2 = 2
        
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query, 20, 0) } returns MoviesLocalDtoList.take(10)
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query, 20, 20) } returns MoviesLocalDtoList.takeLast(10)

        // When
        val result1 = searchRepository.searchMedia(query, page1, null, MediaType.MOVIE)
        val result2 = searchRepository.searchMedia(query, page2, null, MediaType.MOVIE)

        // Then
        assertThat(result1).hasSize(10)
        assertThat(result2).hasSize(10)
        assertThat(result1).isNotEqualTo(result2)
    }

    @Test
    fun `filters work correctly with empty genres list`() = runTest {
        // Given
        val query = "Batman"
        val page = 1
        val filters = MediaFilters(
            genres = emptyList(),
            imdbRating = 7.0f
        )
        
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query, 20, 0) } returns MoviesLocalDtoList

        // When
        val result = searchRepository.searchMedia(query, page, filters, MediaType.MOVIE)

        // Then
        assertThat(result).isNotEmpty()
        // Should only filter by rating, not by genres
    }

    @Test
    fun `filters work correctly with year range`() = runTest {
        // Given
        val query = "Batman"
        val page = 1
        val filters = MediaFilters(
            startYear = 2020,
            endYear = 2024
        )
        
        coEvery { localCacheSearchDataSource.getMoviesByQuery(query, 20, 0) } returns MoviesLocalDtoList

        // When
        val result = searchRepository.searchMedia(query, page, filters, MediaType.MOVIE)

        // Then
        assertThat(result).isNotEmpty()
        // Should only include movies from 2020-2024
    }

    private fun createGenre(id: Int) = search.usecase.search_param.Genre(id, "Test Genre")

    companion object {
        private val ActorsLocalDtoList = listOf(
            ActorsLocalDto(id = 1, name = "Tom Hanks", imagePath = "img", language = "en"),
            ActorsLocalDto(id = 2, name = "Leonardo DiCaprio", imagePath = "img", language = "en")
        )
        
        private val MoviesLocalDtoList = listOf(
            MoviesLocalDto(
                id = 1, 
                title = "Batman Begins", 
                imagePath = "img", 
                releaseYear = 2005, 
                genres = "28, 80", 
                imdbRating = 8.2f, 
                language = "en"
            ),
            MoviesLocalDto(
                id = 2, 
                title = "The Dark Knight", 
                imagePath = "img", 
                releaseYear = 2008, 
                genres = "28, 80, 53", 
                imdbRating = 9.0f, 
                language = "en"
            ),
            MoviesLocalDto(
                id = 3, 
                title = "Batman v Superman", 
                imagePath = "img", 
                releaseYear = 2016, 
                genres = "28, 12", 
                imdbRating = 6.4f, 
                language = "en"
            )
        )
        
        private val TvSeriesLocalDtoList = listOf(
            TvSeriesLocalDto(
                id = 1, 
                title = "Breaking Bad", 
                imagePath = "img", 
                releaseYear = 2008, 
                genres = "18, 80", 
                imdbRating = 9.5f, 
                language = "en"
            ),
            TvSeriesLocalDto(
                id = 2, 
                title = "Better Call Saul", 
                imagePath = "img", 
                releaseYear = 2015, 
                genres = "18, 80", 
                imdbRating = 8.9f, 
                language = "en"
            )
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
        
        private val MoviesRemoteDtoList = listOf(
            MovieSearchDto(
                id = 1,
                title = "Batman Begins",
                posterPath = "/path/to/image",
                releaseDate = "2005-06-15",
                genreIds = listOf(28, 80),
                voteAverage = 8.2f
            ),
            MovieSearchDto(
                id = 2,
                title = "The Dark Knight",
                posterPath = "/path/to/image",
                releaseDate = "2008-07-18",
                genreIds = listOf(28, 80, 53),
                voteAverage = 9.0f
            )
        )
        
        private val TvSeriesRemoteDtoList = listOf(
            TvShowSearchDto(
                id = 1,
                name = "Breaking Bad",
                posterPath = "/path/to/image",
                firstAirDate = "2008-01-20",
                genreIds = listOf(18, 80),
                voteAverage = 9.5f
            ),
            TvShowSearchDto(
                id = 2,
                name = "Better Call Saul",
                posterPath = "/path/to/image",
                firstAirDate = "2015-02-08",
                genreIds = listOf(18, 80),
                voteAverage = 8.9f
            )
        )
        
        private val actorSearchResponse = SearchResponse(
            page = 1,
            results = ActorsRemoteDtoList,
            totalPages = 1,
            totalResults = ActorsRemoteDtoList.size
        )
        
        private val movieSearchResponse = SearchResponse(
            page = 1,
            results = MoviesRemoteDtoList,
            totalPages = 1,
            totalResults = MoviesRemoteDtoList.size
        )
        
        private val tvSeriesSearchResponse = SearchResponse(
            page = 1,
            results = TvSeriesRemoteDtoList,
            totalPages = 1,
            totalResults = TvSeriesRemoteDtoList.size
        )
    }
}