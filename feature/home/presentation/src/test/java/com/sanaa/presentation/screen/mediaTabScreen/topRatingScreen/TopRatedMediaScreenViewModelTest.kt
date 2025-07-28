//package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen
//
//import androidx.paging.PagingData
//import app.cash.turbine.test
//import com.google.common.truth.Truth.assertThat
//import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
//import com.sanaa.presentation.state.GenreUiState
//import com.sanaa.presentation.state.MediaItem
//import com.sanaa.presentation.state.MediaType
//import entity.Genre
//import entity.Movie
//import entity.TvSeries
//import exceptions.NoNetworkException
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import kotlinx.datetime.LocalDate
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import usecase.ManageMovieUseCase
//import usecase.ManageTvSeriesUseCase
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class TopRatedMediaScreenViewModelTest {
//
//    private val manageMovieUseCase: ManageMovieUseCase = mockk(relaxed = true)
//    private val manageTvSeriesUseCase: ManageTvSeriesUseCase = mockk(relaxed = true)
//    private lateinit var viewModel: TopRatedMediaScreenViewModel
//    private val testDispatcher = StandardTestDispatcher()
//
//    @BeforeEach
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//        viewModel = TopRatedMediaScreenViewModel(
//            manageMovieUseCase = manageMovieUseCase,
//            manageTvSeriesUseCase = manageTvSeriesUseCase,
//            dispatcher = testDispatcher
//        )
//    }
//
//    @Test
//    fun `fetchMovieGenres updates state with genres`() = runTest {
//        val genres = listOf(GenreUiState(1, "Action"))
//        coEvery { manageMovieUseCase.getMovieGenres() } returns listOf(Genre(1, "Action"))
//
//        viewModel.fetchMovieGenres()
//
//        viewModel.state.test {
//            awaitItem()
//            val loadedState = awaitItem()
//            assertThat(loadedState.movieGenres).isEqualTo(genres)
//            assertThat(loadedState.isLoading).isFalse()
//        }
//    }
//
//    @Test
//    fun `fetchTvShowGenres updates state with genres`() = runTest {
//        val genres = listOf(GenreUiState(2, "Drama"))
//        coEvery { manageTvSeriesUseCase.getSeriesGenres() } returns listOf(Genre(2, "Drama"))
//
//        viewModel.fetchTvShowGenres()
//
//        viewModel.state.test {
//            awaitItem()
//            val loadedState = awaitItem()
//            assertThat(loadedState.tvShowGenres).isEqualTo(genres)
//            assertThat(loadedState.isLoading).isFalse()
//        }
//    }
//
//    @Test
//    fun `onMediaTabSelection updates selectedMediaType`() = runTest {
//        val mediaType = MediaType.TV_SHOW
//
//        viewModel.onMediaTabSelection(mediaType)
//
//        viewModel.state.test {
//            val item = awaitItem()
//            assertThat(item.selectedMediaType).isEqualTo(mediaType)
//        }
//    }
//
//    @Test
//    fun `onMovieGenreClick triggers fetchMovies`() = runTest {
//        val genreId = 10
//        val expectedFlow = flowOf(
//            PagingData.from(
//                listOf(
//                    MediaItem(
//                        id = 1,
//                        title = "Movie",
//                        "",
//                        mediaType = MediaType.MOVIE
//                    )
//                )
//            )
//        )
//        coEvery { manageMovieUseCase.getTopRatedMovies(any(), any()) } returns listOf(
//            Movie(
//                1, "Movie", "", emptyList(), 0f, 0,
//                LocalDate(1900, 10, 10), "", ""
//            )
//        )
//
//        viewModel.onMovieGenreClick(genreId)
//
//        viewModel.state.test {
//            awaitItem()
//            val state = awaitItem()
//            assertThat(state.movieSelectedGenreId).isEqualTo(genreId)
//        }
//    }
//
//    @Test
//    fun `onTvShowGenreClick triggers fetchTvShows`() = runTest {
//        val genreId = 20
//        coEvery { manageTvSeriesUseCase.getTopRatedTvSeries(any(), any()) } returns listOf(
//            TvSeries(
//                1, "Show", "",
//                LocalDate(1900, 10, 10), emptyList(), 0f, "", 0
//            )
//        )
//
//        viewModel.onTvShowGenreClick(genreId)
//
//        viewModel.state.test {
//            awaitItem()
//            val state = awaitItem()
//            assertThat(state.tvShowSelectedGenreId).isEqualTo(genreId)
//        }
//    }
//
//    @Test
//    fun `onBackClick emits NavigateBack effect`() = runTest {
//        viewModel.onBackClick()
//
//        viewModel.effect.test {
//            val effect = awaitItem()
//            assertThat(effect).isEqualTo(MediaTabScreenEffect.NavigateBack)
//        }
//    }
//
//    @Test
//    fun `onMediaClick emits NavigateToMediaDetails effect`() = runTest {
//        val id = 100
//        val type = MediaType.MOVIE
//
//        viewModel.onMediaClick(id, type)
//
//        viewModel.effect.test {
//            val effect = awaitItem()
//            assertThat(effect).isEqualTo(MediaTabScreenEffect.NavigateToMediaDetails(id, type))
//        }
//    }
//
//    @Test
//    fun `onSaveIconClick shows bottom sheet`() = runTest {
//        val media = MediaItem(
//            1, "Title",
//            imageUrl = "",
//            rating = "",
//            mediaType = MediaType.MOVIE
//        )
//
//        viewModel.onSaveIconClick(media)
//
//        viewModel.state.test {
//            val state = awaitItem()
//            assertThat(state.showBottomSheet).isTrue()
//        }
//    }
//
//    @Test
//    fun `onErrorLoadingData sets noInternetConnection for NoNetworkException`() = runTest {
//        viewModel.onErrorLoadingData(NoNetworkException())
//
//        viewModel.state.test {
//            val state = awaitItem()
//            assertThat(state.isNoInternetConnection).isTrue()
//            assertThat(state.isLoading).isFalse()
//        }
//    }
//}