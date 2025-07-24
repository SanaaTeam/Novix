package com.sanaa.presentation.screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.SearchViewModel.Companion.ACTOR_INDEX
import com.sanaa.presentation.screen.SearchViewModel.Companion.TV_SHOW_INDEX
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.SearchScreenEffects
import com.sanaa.presentation.screen.state.SearchScreenUiState
import exceptions.NoNetworkException
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
import usecase.history.ManageHistoryUseCase
import usecase.history.history_param.SearchHistory
import usecase.search.ManageRecentViewedUseCase
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia
import usecase.search.SearchUseCase
import usecase.search.search_param.MediaFilters
import usecase.search.search_param.MediaType

class SearchViewModelTest {
    private val searchUseCase: SearchUseCase = mockk(relaxed = true)
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase = mockk(relaxed = true)
    private val manageSearchHistoryUseCase: ManageHistoryUseCase = mockk(relaxed = true)
    private lateinit var searchViewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        searchViewModel = SearchViewModel(
            searchUseCase = searchUseCase,
            manageRecentViewedUseCase = manageRecentViewedUseCase,
            manageSearchHistoryUseCase = manageSearchHistoryUseCase,
            dispatcher = testDispatcher,
        )
    }

    @Test
    fun `loadMediaByTab should load actors when ACTOR_INDEX is selected`() = runTest {
        val actorList = listOf(
            ActorUiModel(id = 1, name = "John Doe", imageUrl = "url1"),
            ActorUiModel(id = 2, name = "Jane Smith", imageUrl = "url2")
        )
        TestActorPagingSource(actorList)

        searchViewModel.updateState { it.copy(selectedTabIndex = ACTOR_INDEX) }

        val method =
            SearchViewModel::class.java.getDeclaredMethod("loadMediaByTab", String::class.java)
        method.isAccessible = true
        method.invoke(searchViewModel, "john")

        searchViewModel.actorsPagingData.test {
            val result = awaitItem()
            assertThat(result).isNotNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `load tv shows when tab index is TV_SHOW_INDEX and query is not blank`() = runTest {
        val actorList = listOf(
            ActorUiModel(id = 1, name = "John Doe", imageUrl = "url1"),
            ActorUiModel(id = 2, name = "Jane Smith", imageUrl = "url2")
        )

        TestActorPagingSource(actorList)
        searchViewModel.updateState { it.copy(selectedTabIndex = TV_SHOW_INDEX) }

        val method =
            SearchViewModel::class.java.getDeclaredMethod("loadMediaByTab", String::class.java)
        method.isAccessible = true
        method.invoke(searchViewModel, "john")

        searchViewModel.actorsPagingData.test {
            val result = awaitItem()
            assertThat(result).isNotNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeRecentViewedItems() should first stop loading when start clear recent viewed item`() =
        runTest {
            searchViewModel

            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `observeRecentViewedItems()  when start clear recent viewed item`() =
        runTest {
            val viewedMedias = listOf(
                RecentViewedMedia(1, "https://image.com", MediaType.MOVIE, false)
            )
            coEvery { manageRecentViewedUseCase.getRecentViewed() } returns flowOf(viewedMedias)

            searchViewModel.observeRecentViewedItems()

            searchViewModel.state.test {
                awaitItem()
                val item = awaitItem()
                val expected =
                    SearchScreenUiState(
                        isLoading = true,
                        error = null,
                        recentViewedMedia = viewedMedias.map {
                            RecentViewedUiModel(
                                id = it.id,
                                imageUrl = it.posterImageUrl,
                                mediaType = MediaTypeUi.valueOf(it.mediaType.name),
                                isSaved = it.isSaved
                            )
                        }
                    )
                assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `observeRecentSearchHistory() should first stop loading when start clear recent viewed item`() =
        runTest {
            // init Then
            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `observeRecentSearchHistory()  when start clear recent viewed item`() =
        runTest {
            val timestamp = Instant.fromEpochMilliseconds(1234567890L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val resentSearchHistories = listOf(
                SearchHistory(1, "Movie", timestamp = timestamp)
            )
            coEvery { manageSearchHistoryUseCase.getSearchHistory() } returns flowOf(
                resentSearchHistories
            )

            searchViewModel.observeRecentSearchHistory()

            searchViewModel.state.test {
                awaitItem()
                val item = awaitItem()
                val expected =
                    SearchScreenUiState(
                        isLoading = true,
                        error = null,
                        recentSearchQueries = resentSearchHistories.map {
                            RecentSearchUiModel(
                                id = it.id,
                                title = it.query
                            )
                        }
                    )
                assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `onClearRecentViewClicked() should first stop loading when start clear recent viewed item`() =
        runTest {
            searchViewModel.onClearRecentViewClicked()

            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentViewClicked() stop loading when clear recent viewed item success`() =
        runTest {
            coEvery { manageRecentViewedUseCase.clearRecentViewed() } just Runs

            searchViewModel.onClearRecentViewClicked()

            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentViewClicked() should show error when clear recent viewed item failed`() =
        runTest {
            val errorMessage = "Some Error"
            coEvery { manageRecentViewedUseCase.clearRecentViewed() } throws Exception(errorMessage)

            searchViewModel.onClearRecentViewClicked()

            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false, error = errorMessage)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentSearchClicked() should first stop loading when start clear recent search`() =
        runTest {
            searchViewModel.onClearRecentSearchClicked()

            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `onClearRecentSearchClicked() stop loading when clear search history success`() =
        runTest {
            coEvery { manageSearchHistoryUseCase.clearSearchHistory() } just Runs

            searchViewModel.onClearRecentSearchClicked()

            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentSearchClicked() should show error when clear search history failed`() =
        runTest {
            val errorMessage = "Unknown error"
            coEvery { manageSearchHistoryUseCase.clearSearchHistory() } throws Exception(
                errorMessage
            )

            searchViewModel.onClearRecentSearchClicked()

            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false, error = errorMessage)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onTabSelected() should set the selected tab index`() = runTest {
        val index = SearchScreenUiState.TV_SHOW_INDEX

        searchViewModel.onTabSelected(index)

        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(
                selectedTabIndex = index,
                isLoading = true,
                error = null
            )
            assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onTabSelected() should update selectedTabIndex and load media`() = runTest {
        val initialState = searchViewModel.state.value
        assertThat(initialState.selectedTabIndex)
            .isNotEqualTo(SearchScreenUiState.TV_SHOW_INDEX)

        searchViewModel.onTabSelected(SearchScreenUiState.TV_SHOW_INDEX)

        searchViewModel.state.test {
            val item = awaitItem()
            assertThat(item.selectedTabIndex).isEqualTo(SearchScreenUiState.TV_SHOW_INDEX)
            assertThat(item.isLoading).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTabSelected() should load movies when movie tap selected and show filter button`() =
        runTest {
            val index = SearchScreenUiState.MOVIE_INDEX
            val uiState = searchViewModel.state
            val movieName = "Movie"
            val page = 1
            val movies = listOf(SearchMovieOutput(1, movieName, "https://image.com"))
            searchViewModel.onSearchQueryChanged(movieName)
            coEvery {
                searchUseCase.searchMovies(
                    uiState.value.searchQuery,
                    page = page,
                    filters = uiState.value.filters
                )
            } returns movies

            searchViewModel.onTabSelected(index)

            searchViewModel.state.test {
                awaitItem()
                val item = awaitItem()
                val expected = SearchScreenUiState(
                    searchQuery = movieName,
                    selectedTabIndex = index,
                    isLoading = false,
                )
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onTabSelected() should load tv shows when tv show tap selected and show filter button`() =
        runTest {
            val index = SearchScreenUiState.TV_SHOW_INDEX
            val uiState = searchViewModel.state
            val tvShowName = "TvShow"
            val page = 1
            val tvShows = listOf(SearchTvSeriesOutput(1, tvShowName, "https://image.com"))
            searchViewModel.onSearchQueryChanged(tvShowName)
            coEvery {
                searchUseCase.searchTvShows(
                    uiState.value.searchQuery,
                    page = page,
                    filters = uiState.value.filters
                )
            } returns tvShows

            searchViewModel.onTabSelected(index)

            searchViewModel.state.test {
                awaitItem()
                val item = awaitItem()
                val expected = SearchScreenUiState(
                    searchQuery = tvShowName,
                    selectedTabIndex = index,
                    isLoading = false,
                )
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onTabSelected() should update tab and load media if new tab is selected`() = runTest {
        val index = SearchScreenUiState.TV_SHOW_INDEX

        searchViewModel.onTabSelected(index)

        searchViewModel.state.test {
            val item = awaitItem()
            assertThat(item.selectedTabIndex).isEqualTo(index)
        }
    }

    @Test
    fun `onTabSelected() should load actors when actor tap selected and hide filter button`() =
        runTest {
            val index = SearchScreenUiState.ACTOR_INDEX
            val uiState = searchViewModel.state
            val actorName = "TvShow"
            val page = 1
            val actors = listOf(SearchActorOutput(1, actorName, "https://image.com"))
            searchViewModel.onSearchQueryChanged(actorName)
            coEvery {
                searchUseCase.searchActors(
                    uiState.value.searchQuery, page
                )
            } returns actors

            searchViewModel.onTabSelected(index)

            searchViewModel.state.test {
                awaitItem()
                val item = awaitItem()
                val expected = SearchScreenUiState(
                    searchQuery = actorName,
                    selectedTabIndex = index,
                    isLoading = false,
                )
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onSearchQueryChanged() should set state search query`() = runTest {
        val query = ""

        searchViewModel.onSearchQueryChanged(query)

        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(isLoading = true, searchQuery = query)
            assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `retrySearch() should reset state search query`() = runTest {
        val query = ""

        searchViewModel.retrySearch()

        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(isLoading = true, searchQuery = query)
            assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onFilterApplied() should set state filters`() = runTest {
        val filters = MediaFilters()

        searchViewModel.onFilterApplied(filters)

        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(isLoading = true, filters = filters)
            assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onFilterApplied() should search movie when apply filters`() = runTest {
        val filters = MediaFilters()
        val index = SearchScreenUiState.MOVIE_INDEX
        val movieName = "Movie"
        searchViewModel.onSearchQueryChanged(movieName)

        searchViewModel.onFilterApplied(filters)

        searchViewModel.state.test {
            awaitItem()
            val item = awaitItem()
            val expected = SearchScreenUiState(
                searchQuery = movieName,
                selectedTabIndex = index,
                isLoading = false,
                filters = filters
            )
            assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onSearchResultMediaClicked() should doing nothing right now`() = runTest {
        val viewed = RecentViewedUiModel(
            1, "https//image.com", MediaTypeUi.MOVIE, false
        )

        searchViewModel.onSearchResultMediaClicked(viewed)

        searchViewModel.state.test {
            awaitItem()
            val item = awaitItem()
            val expected = SearchScreenUiState()
            assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onDeleteRecentSearchItem() should first stop loading when start clear recent viewed item`() =
        runTest {
            val id = 1

            searchViewModel.onDeleteRecentSearchItem(id)

            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `onRecentSearchItemClicked() should set state search query`() = runTest {
        val query = ""

        searchViewModel.onRecentSearchItemClicked(query)

        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(isLoading = true, searchQuery = query)
            assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun ` onDeleteRecentSearchItem() should update noInternetConnection = true when call api failed with NoNetworkException`() =
        runTest {
            val id = 1
            coEvery { manageSearchHistoryUseCase.removeSearchHistory(id) } throws NoNetworkException()

            searchViewModel.onDeleteRecentSearchItem(id)

            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false, noInternetConnection = true)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentSearchClicked() should show Unknown error when request failed with unknown exception`() =
        runTest {
            coEvery { manageSearchHistoryUseCase.clearSearchHistory() } throws Exception()

            searchViewModel.onClearRecentSearchClicked()

            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false, error = "Unknown error")
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onFilterClicked() should show bottom sheet when filter button clicked`() =
        runTest {
            searchViewModel.onFilterClicked()

            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(showBottomSheet = true)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onBottomSheetDragged() should hide bottom sheet when filter dragged`() =
        runTest {
            searchViewModel.onBottomSheetDragged()

            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(showBottomSheet = false)
                assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onActorClicked should emit NavigateToActorDetails effect`() = runTest {

        val actorId = 7


        searchViewModel.onActorClicked(actorId)


        searchViewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(SearchScreenEffects.NavigateToActorDetails(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun `onSearchResultMediaClicked should emit NavigateToMovieDetails when media is MOVIE`() = runTest {

        val viewed = RecentViewedUiModel(10, "url", MediaTypeUi.MOVIE, false)


        searchViewModel.onSearchResultMediaClicked(viewed)


        searchViewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(SearchScreenEffects.NavigateToMovieDetails(viewed.id))
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun `onSearchResultMediaClicked should emit NavigateToTvShowDetails when media is TV`() = runTest {
        val viewed = RecentViewedUiModel(22, "url", MediaTypeUi.TV_SERIES, false)

        searchViewModel.onSearchResultMediaClicked(viewed)

        searchViewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(SearchScreenEffects.NavigateToTvShowDetails(viewed.id))
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun `onRecentViewedMediaClicked should emit NavigateToMovieDetails when media is MOVIE`() = runTest {
        val viewed = RecentViewedUiModel(100, "url", MediaTypeUi.MOVIE, false)

        searchViewModel.onRecentViewedMediaClicked(viewed)

        searchViewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(SearchScreenEffects.NavigateToMovieDetails(viewed.id))
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun `onRecentViewedMediaClicked should emit NavigateToTvShowDetails when media is TV_SERIES`() = runTest {
        val viewed = RecentViewedUiModel(200, "url", MediaTypeUi.TV_SERIES, false)

        searchViewModel.onRecentViewedMediaClicked(viewed)

        searchViewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(SearchScreenEffects.NavigateToTvShowDetails(viewed.id))
            cancelAndIgnoreRemainingEvents()
        }
    }


}

class TestActorPagingSource(
    private val data: List<ActorUiModel>,
) : PagingSource<Int, ActorUiModel>() {
    override fun getRefreshKey(state: PagingState<Int, ActorUiModel>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ActorUiModel> {
        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = null
        )
    }
}
