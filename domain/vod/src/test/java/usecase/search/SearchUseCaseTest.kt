package usecase.search

import com.google.common.truth.Truth.assertThat
import entity.Actor
import entity.Movie
import entity.TvShow
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.HistoryRepository
import repository.SearchRepository
import kotlin.time.Duration.Companion.minutes

class SearchUseCaseTest {
    private var searchRepository: SearchRepository = mockk(relaxed = true)
    private var historyRepository: HistoryRepository = mockk(relaxed = true)
    private lateinit var searchUseCase: SearchUseCase

    @BeforeEach
    fun setUp() {
        searchUseCase = SearchUseCase(searchRepository, historyRepository)
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
                historyRepository.addSearchHistory(query)
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
    fun `searchMovies() should throw NovixAppException when try to search a movie failed`() =
        runTest {
            // Given
            val query = "Sam"
            val page = 1
            coEvery {
                searchRepository.searchMovies(query, page)
            } throws NovixAppException("")

            // When, Then
            assertThrows<NovixAppException> {
                searchUseCase.searchMovies(query, page)
            }
        }

    @Test
    fun `searchTvShows() should call addSearchHistory() from SearchHistoryRepository when search a tv show`() =
        runTest {
            // Given
            val query = "Tv show"
            val page = 1

            // When
            searchUseCase.searchTvShows(query, page)

            // Then
            coVerify {
                historyRepository.addSearchHistory(query)
            }
        }

    @Test
    fun `searchTvShows() should return tv show search result`() =
        runTest {
            // Given
            val query = "Tv show"
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
    fun `searchTvShows() should throw RetrievingDataFailureException when try to search an tv show failed`() =
        runTest {
            // Given
            val query = "Sam"
            val page = 1
            coEvery {
                searchRepository.searchTvShows(query, page)
            } throws NovixAppException("")

            // When, Then
            assertThrows<NovixAppException> {
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
                historyRepository.addSearchHistory(query)
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
    fun `searchActors() should throw NovixAppException when try to search an actor failed`() =
        runTest {
            // Given
            val query = "Sam"
            val page = 1
            coEvery {
                searchRepository.searchActors(query, page)
            } throws NovixAppException("")

            // When, Then
            assertThrows<NovixAppException> {
                searchUseCase.searchActors(query, page)
            }
        }


    private companion object {
        private val dummyActor = listOf(
            Actor(
                id = 1,
                name = "title",
                imageUrl = "",
                department = "",
                character = "",
                birthDate = LocalDate(1, 1, 1),
                deathDate = LocalDate(1, 1, 1),
                placeOfBirth = "",
                biography = "",
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
                trailerUrl = "",
                rating = 0
            )
        )

        val dummyTvShow = listOf(
            TvShow(
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