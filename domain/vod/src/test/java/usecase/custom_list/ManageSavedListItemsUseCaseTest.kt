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

class ManageSavedListItemsUseCaseTest {

    private val savedListRepository: SavedListRepository = mockk(relaxed = true)
    private lateinit var manageSavedListItemsUseCase: ManageSavedListItemsUseCase

    @BeforeEach
    fun setUp() {
        manageSavedListItemsUseCase = ManageSavedListItemsUseCase(savedListRepository)
    }

    @Test
    fun `getAllItemsInSavedList should call repository and return movies`() = runTest {
        coEvery { savedListRepository.getAllMoviesInList(LIST_ID) } returns DUMMY_MOVIES

        val result = manageSavedListItemsUseCase.getAllItemsInSavedList(LIST_ID)

        assertThat(result).isEqualTo(DUMMY_MOVIES)
    }

    @Test
    fun `addMovieToSavedList should call repository and return true`() = runTest {
        coEvery { savedListRepository.addMovieToList(LIST_ID, MOVIE_ID) } returns true

        val result = manageSavedListItemsUseCase.addMovieToSavedList(LIST_ID, MOVIE_ID)

        assertThat(result).isTrue()
        coVerify(exactly = 1) { savedListRepository.addMovieToList(LIST_ID, MOVIE_ID) }
    }

    @Test
    fun `removeMovieFromSavedList should call repository and return true`() = runTest {
        coEvery { savedListRepository.removeMovieFromList(LIST_ID, MOVIE_ID) } returns true

        val result = manageSavedListItemsUseCase.removeMovieFromSavedList(LIST_ID, MOVIE_ID)

        assertThat(result).isTrue()
        coVerify(exactly = 1) { savedListRepository.removeMovieFromList(LIST_ID, MOVIE_ID) }
    }

    private companion object {
        const val LIST_ID = 1
        const val MOVIE_ID = 278

        val DUMMY_MOVIES = listOf(
            Movie(
                id = MOVIE_ID,
                posterImageUrl = "url1",
                title = "Movie 1",
                genres = emptyList(),
                imdbRating = 8.5f,
                duration = null,
                releaseDate = LocalDate(2023, 5, 20),
                rating = null
            )
        )
    }
}