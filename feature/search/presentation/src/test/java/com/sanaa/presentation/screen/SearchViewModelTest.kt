package com.sanaa.presentation.screen

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.sanaa.presentation.state.ActorUiModel
import com.sanaa.presentation.state.MovieUiModel
import com.sanaa.presentation.state.SearchScreenUiState
import com.sanaa.presentation.state.TvShowUiModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.ClearRecentViewedUseCase
import usecase.ClearSearchHistoryUseCase
import usecase.GetRecentViewedUseCase
import usecase.GetSearchHistoryUseCase
import usecase.SearchActorsUseCase
import usecase.SearchMoviesUseCase
import usecase.SearchTvSeriesUseCase
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.RecentViewedMedia
import usecase.search.SearchActorOutput
import usecase.search.SearchHistory
import usecase.search.SearchMediaOutput

class SearchViewModelTest {
    private val searchMoviesUseCase: SearchMoviesUseCase = mockk(relaxed = true)
    private val searchTvSeriesUseCase: SearchTvSeriesUseCase = mockk(relaxed = true)
    private val searchActorsUseCase: SearchActorsUseCase = mockk(relaxed = true)
    private val getRecentViewedUseCase: GetRecentViewedUseCase = mockk(relaxed = true)
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase = mockk(relaxed = true)
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase = mockk(relaxed = true)
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase = mockk(relaxed = true)
    private lateinit var searchViewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        searchViewModel = SearchViewModel(
            searchMoviesUseCase,
            searchTvSeriesUseCase,
            searchActorsUseCase,
            getRecentViewedUseCase,
            getSearchHistoryUseCase,
            clearRecentViewedUseCase,
            clearSearchHistoryUseCase,
            testDispatcher,
        )
    }

    @Test
    fun `loadResentViewedImageList() should first stop loading when start clear recent viewed item`() =
        runTest {
            // When
            searchViewModel.loadResentViewedImageList()

            // Then
            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `loadResentViewedImageList()  when start clear recent viewed item`() =
        runTest {
            // Given
            val viewedMedias = listOf(
                RecentViewedMedia(1, "https://image.com", MediaType.MOVIE, false)
            )
            coEvery { getRecentViewedUseCase.execute() } returns flowOf(viewedMedias)

            // When
            searchViewModel.loadResentViewedImageList()

            // Then
            searchViewModel.state.test {
                awaitItem()
                val item = awaitItem()
                val expected =
                    SearchScreenUiState(
                        isLoading = true,
                        error = null,
                        resentViewedImageList = viewedMedias.map { it.posterImageUrl }
                    )
                Truth.assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `loadResentSearchTitleList() should first stop loading when start clear recent viewed item`() =
        runTest {
            // When
            searchViewModel.loadResentSearchTitleList()

            // Then
            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `loadResentSearchTitleList()  when start clear recent viewed item`() =
        runTest {
            // Given
            val timestamp = Instant.fromEpochMilliseconds(1234567890L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val resentSearchHistories = listOf(
                SearchHistory(1, "Movie", timestamp = timestamp)
            )
            coEvery { getSearchHistoryUseCase.execute() } returns flowOf(resentSearchHistories)

            // When
            searchViewModel.loadResentSearchTitleList()

            // Then
            searchViewModel.state.test {
                awaitItem()
                val item = awaitItem()
                val expected =
                    SearchScreenUiState(
                        isLoading = true,
                        error = null,
                        resentSearchTitleList = resentSearchHistories.map { it.query }
                    )
                Truth.assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `onClearRecentViewClicked() should first stop loading when start clear recent viewed item`() =
        runTest {
            // When
            searchViewModel.onClearRecentViewClicked()

            // Then
            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentViewClicked() stop loading when clear recent viewed item success`() =
        runTest {
            // Given
            coEvery { clearRecentViewedUseCase.execute() } just Runs

            // When
            searchViewModel.onClearRecentViewClicked()

            // Then
            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentViewClicked() should show error when clear recent viewed item failed`() =
        runTest {
            // Given
            val errorMessage = "Some Error"
            coEvery { clearRecentViewedUseCase.execute() } throws Exception(errorMessage)

            // When
            searchViewModel.onClearRecentViewClicked()

            // Then
            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false, error = errorMessage)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentSearchClicked() should first stop loading when start clear recent search`() =
        runTest {
            // When
            searchViewModel.onClearRecentSearchClicked()

            // Then
            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `onClearRecentSearchClicked() stop loading when clear search history success`() =
        runTest {
            // Given
            coEvery { clearSearchHistoryUseCase.execute() } just Runs

            // When
            searchViewModel.onClearRecentSearchClicked()

            // Then
            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentSearchClicked() should show error when clear search history failed`() =
        runTest {
            // Given
            val errorMessage = "Unknown error"
            coEvery { clearSearchHistoryUseCase.execute() } throws Exception(errorMessage)

            // When
            searchViewModel.onClearRecentSearchClicked()

            // Then
            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false, error = errorMessage)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onTabSelected() should set the selected tab index`() = runTest {
        // Given
        val index = SearchViewModel.TV_SHOW_INDEX

        // When
        searchViewModel.onTabSelected(index)

        // Then
        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(
                selectedTabIndex = index,
                isLoading = true,
                error = null
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onTabSelected() should load movies when movie tap selected`() = runTest {
        // Given
        val index = SearchViewModel.MOVIE_INDEX
        val uiState = searchViewModel.state
        val movies = listOf(SearchMediaOutput(1, "Movie", "https://image.com", false))
        coEvery {
            searchMoviesUseCase.execute(
                uiState.value.searchQuery,
                filters = uiState.value.filters
            )
        } returns movies

        // When
        searchViewModel.onTabSelected(index)

        // Then
        searchViewModel.state.test {
            awaitItem()
            val item = awaitItem()
            val expected = SearchScreenUiState(
                selectedTabIndex = index,
                isLoading = false,
                movies = movies.map { MovieUiModel(it.id, it.title, it.posterImageUrl, "") }
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onTabSelected() should load tv shows when tv show tap selected`() = runTest {
        // Given
        val index = SearchViewModel.TV_SHOW_INDEX
        val uiState = searchViewModel.state
        val tvShows = listOf(SearchMediaOutput(1, "Tv Seris", "https://image.com", false))
        coEvery {
            searchTvSeriesUseCase.execute(
                uiState.value.searchQuery,
                filters = uiState.value.filters
            )
        } returns tvShows

        // When
        searchViewModel.onTabSelected(index)

        // Then
        searchViewModel.state.test {
            awaitItem()
            val item = awaitItem()
            val expected = SearchScreenUiState(
                selectedTabIndex = index,
                isLoading = false,
                tvShows = tvShows.map { TvShowUiModel(it.id, it.title, it.posterImageUrl, "") }
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onTabSelected() should load actors when actor tap selected`() = runTest {
        // Given
        val index = SearchViewModel.ACTOR_INDEX
        val uiState = searchViewModel.state
        val actors = listOf(SearchActorOutput(1, "Tv Seris", "https://image.com"))
        coEvery {
            searchActorsUseCase.execute(
                uiState.value.searchQuery,
            )
        } returns actors

        // When
        searchViewModel.onTabSelected(index)

        // Then
        searchViewModel.state.test {
            awaitItem()
            val item = awaitItem()
            val expected = SearchScreenUiState(
                selectedTabIndex = index,
                isLoading = false,
                actors = actors.map { ActorUiModel(it.id, it.name, it.profileImageUrl) }
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onSearchQueryChanged() should set state search query`() = runTest {
        // Given
        val query = ""

        // When
        searchViewModel.onSearchQueryChanged(query)

        // Then
        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(isLoading = true, searchQuery = query)
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onFilterApplied() should set state filters`() = runTest {
        // Given
        val filters = MediaFilters()

        // When
        searchViewModel.onFilterApplied(filters)

        // Then
        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(isLoading = true, filters = filters)
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

}