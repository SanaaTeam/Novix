package usecase.custom_list

import com.google.common.truth.Truth.assertThat
import entity.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SavedListRepository
import kotlin.time.Duration.Companion.minutes

class ManageSavedListItemsUseCaseTest {

    private val savedListRepository: SavedListRepository = mockk(relaxed = true)
    private lateinit var manageSavedListItemsUseCase: ManageSavedListItemsUseCase

    @BeforeEach
    fun setUp() {
        manageSavedListItemsUseCase = ManageSavedListItemsUseCase(savedListRepository)
    }

    @Test
    fun `getAllItemsInSavedList should call repository and return movies`() = runTest {
        coEvery { savedListRepository.getMoviesInList(LIST_ID, PAGE) } returns DUMMY_MOVIES

        val result = manageSavedListItemsUseCase.getItemsInSavedList(LIST_ID, PAGE)

        assertThat(result).isEqualTo(DUMMY_MOVIES)
    }

    @Test
    fun `addMovieToSavedList should call repository`() = runTest {

        manageSavedListItemsUseCase.addMovieToSavedList(LIST_ID, MOVIE_ID)

        coVerify(exactly = 1) { savedListRepository.addMovieToList(LIST_ID, MOVIE_ID) }
    }

    @Test
    fun `removeMovieFromSavedList should call repository`() = runTest {

        manageSavedListItemsUseCase.removeMovieFromSavedList(LIST_ID, MOVIE_ID)

        coVerify(exactly = 1) { savedListRepository.removeMovieFromList(LIST_ID, MOVIE_ID) }
    }

    private companion object {
        const val LIST_ID = 1
        const val PAGE = 1
        const val MOVIE_ID = 278

        val DUMMY_MOVIES = listOf(
            Movie(
                id = MOVIE_ID,
                posterImageUrl = "url1",
                title = "Movie 1",
                genres = emptyList(),
                imdbRating = 8.5f,
                duration = 1.minutes,
                releaseDate = LocalDate(2023, 5, 20),
                rating = 1,
                overview = "",
                trailerUrl = ""
            )
        )
    }
}