package usecase.custom_list

import entity.Movie
import entity.TvSeries
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedItem
import usecase.search.search_param.MediaType

class ManageSavedListItemsUseCaseTest {
    private val savedListRepository: SavedListRepository = mockk(relaxed = true)
    private lateinit var manageSavedListsUseCase: ManageSavedListsUseCase

    @BeforeEach
    fun setUp() {
        manageSavedListsUseCase = ManageSavedListsUseCase(savedListRepository)
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
