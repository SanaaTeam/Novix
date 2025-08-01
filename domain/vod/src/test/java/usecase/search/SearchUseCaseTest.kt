package usecase.search

import com.google.common.truth.Truth.assertThat
import entity.Actor
import entity.Movie
import entity.TvSeries
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.SearchHistoryRepository
import repository.SearchRepository
import kotlin.time.Duration.Companion.minutes

class SearchUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var searchUseCase: SearchUseCase

    @BeforeEach
    fun setUp() {
        searchUseCase = SearchUseCase(searchRepository, searchHistoryRepository)
    }

    @Test
    fun `searchMovies() should call addSearchHistory() from SearchHistoryRepository when search a movie`() =
        runTest {
            // Given
            val query = "Movie"
            val page = 1

            // When
            searchUseCase.searchMovies(query, page)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistory(query)
            }
        }

    @Test
    fun `searchMovies() should return movie search result`() =
        runTest {
            // Given
            val query = "Movie"
            val page = 1
            coEvery {
                searchRepository.searchMovies(query, page)
            } returns dummyMovie

            // When
            val result = searchUseCase.searchMovies(query, page)

            // Then
            assertThat(result).isEqualTo(dummyMovie)
        }

    @Test
    fun `searchMovies() should throw RetrievingDataFailureException when try to search a movie failed`() =
        runTest {
            // Given
            val query = "Sam"
            val page = 1
            coEvery {
                searchRepository.searchMovies(query, page)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchUseCase.searchMovies(query, page)
            }
        }

    @Test
    fun `searchTvShows() should call addSearchHistory() from SearchHistoryRepository when search a tv series`() =
        runTest {
            // Given
            val query = "TvSeries"
            val page = 1

            // When
            searchUseCase.searchTvShows(query, page)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistory(query)
            }
        }

    @Test
    fun `searchTvShows() should return tv series search result`() =
        runTest {
            // Given
            val query = "Tv Series"
            val page = 1
            coEvery {
                searchRepository.searchTvShows(query, page)
            } returns dummyTvShow

            // When
            val result = searchUseCase.searchTvShows(query, page)

            // Then
            assertThat(result).isEqualTo(dummyTvShow)
        }

    @Test
    fun `searchTvShows() should throw RetrievingDataFailureException when try to search an tv series failed`() =
        runTest {
            // Given
            val query = "Sam"
            val page = 1
            coEvery {
                searchRepository.searchTvShows(query, page)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchUseCase.searchTvShows(query, page)
            }
        }


    @Test
    fun `searchActors() should call addSearchHistory() from SearchHistoryRepository when search an actor`() =
        runTest {
            // Given
            val query = "Actor"
            val page = 1

            // When
            searchUseCase.searchActors(query, page)

            // Then
            coVerify {
                searchHistoryRepository.addSearchHistory(query)
            }
        }


    @Test
    fun `searchActors() should return actor search result when search`() =
        runTest {
            // Given
            val query = "Actor"
            val page = 1
            coEvery { searchRepository.searchActors(query, page) } returns dummyActor

            // When
            val result = searchUseCase.searchActors(query, page)

            // Then
            assertThat(result).isEqualTo(dummyActor)
        }

    @Test
    fun `searchActors() should throw RetrievingDataFailureException when try to search an actor failed`() =
        runTest {
            // Given
            val query = "Sam"
            val page = 1
            coEvery {
                searchRepository.searchActors(query, page)
            } throws RetrievingDataFailureException("")

            // When, Then
            assertThrows<RetrievingDataFailureException> {
                searchUseCase.searchActors(query, page)
            }
        }


    private companion object {
        private val dummyActor = listOf(
            Actor(
                id = 1,
                name = "title",
                imageUrl = "",
                region = null,
                lastShow = null,
                gender = Actor.Gender.MALE,
                department = null,
                character = null,
                birthDate = null,
                deathDate = null,
                placeOfBirth = null,
                biography = null,
            )
        )

        private val dummyMovie = listOf(
            Movie(
                id = 1,
                title = "title",
                posterImageUrl = "imageUrl",
                genres = emptyList(),
                imdbRating = 1f,
                duration = 10.minutes,
                releaseDate = LocalDate(1990, 10, 10),
                overview = "null",
                trailerUrl = null,
                rating = 0
            )
        )

        val dummyTvShow = listOf(
            TvSeries(
                id = 1,
                title = "title",
                posterImageUrl = "imageUrl",
                overview = "",
                releaseDate = LocalDate(1990, 10, 10),
                genres = emptyList(),
                imdbRating = 9f,
                seasonsCount = 1,
                rating = 0
            )
        )
    }
}