package usecase.custom_list

import com.google.common.truth.Truth.assertThat
import entity.Movie
import entity.TvSeries
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedItem
import usecase.search.search_param.MediaType

class ManageSavedListItemsUseCaseTest {
    private val savedListRepository: SavedListRepository = mockk(relaxed = true)
    private lateinit var manageSavedListItemsUseCase: ManageSavedListItemsUseCase

    @BeforeEach
    fun setUp() {
        manageSavedListItemsUseCase = ManageSavedListItemsUseCase(savedListRepository)
    }

    @Test
    fun shouldReturnAllItems_whenGetAllItemsInSavedListIsCalled() = runTest {
        coEvery { savedListRepository.getAllItemsInList(dummyListId) } returns dummySavedItems

        val result = manageSavedListItemsUseCase.getAllItemsInSavedList(dummyListId)

        assertThat(result).isEqualTo(dummySavedItems)
    }

    @Test
    fun shouldReturnMovies_whenGetMoviesInSavedListIsCalled() = runTest {
        coEvery { savedListRepository.getMoviesInList(dummyListId) } returns dummyMovies

        val result = manageSavedListItemsUseCase.getMoviesInSavedList(dummyListId)

        assertThat(result).isEqualTo(dummyMovies)
    }

    @Test
    fun shouldReturnTvSeries_whenGetTvSeriesInSavedListIsCalled() = runTest {
        coEvery { savedListRepository.getTvSeriesInList(dummyListId) } returns dummyTvSeries

        val result = manageSavedListItemsUseCase.getTvSeriesInSavedList(dummyListId)

        assertThat(result).isEqualTo(dummyTvSeries)
    }

    @Test
    fun shouldReturnTrue_whenAddMovieToSavedListIsSuccessful() = runTest {
        coEvery { savedListRepository.addMovieToList(dummyListId, dummyMovieId) } returns true

        val result = manageSavedListItemsUseCase.addMovieToSavedList(dummyListId, dummyMovieId)

        assertTrue(result)
    }

    @Test
    fun shouldReturnTrue_whenAddTvSeriesToSavedListIsSuccessful() = runTest {
        coEvery { savedListRepository.addTvSeriesToList(dummyListId, dummyTvSeriesId) } returns true

        val result =
            manageSavedListItemsUseCase.addTvSeriesToSavedList(dummyListId, dummyTvSeriesId)

        assertTrue(result)
    }

    @Test
    fun shouldCallRemoveItemFromList_whenRemoveItemFromSavedListIsCalled() = runTest {
        coEvery { savedListRepository.removeItemFromList(dummyItemId) } returns Unit

        manageSavedListItemsUseCase.removeItemFromSavedList(dummyItemId)

        coVerify(exactly = 1) { savedListRepository.removeItemFromList(dummyItemId) }
    }

    companion object {

        private val dummyListId = 1
        private val dummyMovieId = 101
        private val dummyTvSeriesId = 202
        private val dummyItemId = 303

        private val dummySavedItems = listOf(
            SavedItem(id = 1, posterImageUrl = "url1", mediaType = MediaType.MOVIE, isSaved = true),
            SavedItem(
                id = 2,
                posterImageUrl = "url2",
                mediaType = MediaType.TV_SERIES,
                isSaved = true
            )
        )

        private val dummyMovies = listOf(
            Movie(
                id = 1,
                posterImageUrl = "url1",
                title = "Movie 1",
                genres = emptyList(),
                imdbRating = 7.5f,
                duration = null,
                releaseDate = LocalDate(2022, 11, 3),
                rating = null
            )
        )

        private val dummyTvSeries = listOf(
            TvSeries(
                id = 1,
                title = "Series 1",
                overview = "",
                releaseDate = LocalDate(2022, 11, 3),
                genres = emptyList(),
                imdbRating = 8.0f,
                posterImageUrl = "url",
                seasonsCount = 3,
                rating = null
            )
        )
    }
}
