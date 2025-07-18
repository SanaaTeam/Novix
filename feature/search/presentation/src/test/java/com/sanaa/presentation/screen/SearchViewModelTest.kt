package com.sanaa.presentation.screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.sanaa.presentation.screen.SearchViewModel.Companion.ACTOR_INDEX
import com.sanaa.presentation.screen.SearchViewModel.Companion.TV_SHOW_INDEX
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
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
import search.usecase.AddRecentViewedUseCase
import search.usecase.ClearRecentViewedUseCase
import search.usecase.ClearSearchHistoryUseCase
import search.usecase.GetRecentViewedUseCase
import search.usecase.GetSearchHistoryUseCase
import search.usecase.RemoveSearchHistoryUseCase
import search.usecase.SearchActorsUseCase
import search.usecase.SearchMoviesUseCase
import search.usecase.SearchTvSeriesUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.RecentViewedMedia
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchHistory
import search.usecase.search_param.SearchMovieOutput
import search.usecase.search_param.SearchTvSeriesOutput
import kotlin.test.Ignore

class SearchViewModelTest {
    private val searchMoviesUseCase: SearchMoviesUseCase = mockk(relaxed = true)
    private val searchTvSeriesUseCase: SearchTvSeriesUseCase = mockk(relaxed = true)
    private val searchActorsUseCase: SearchActorsUseCase = mockk(relaxed = true)
    private val getRecentViewedUseCase: GetRecentViewedUseCase = mockk(relaxed = true)
    private val addRecentViewedUseCase: AddRecentViewedUseCase = mockk(relaxed = true)
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase = mockk(relaxed = true)
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase = mockk(relaxed = true)
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase = mockk(relaxed = true)
    private val deleteSearchItemUseCase: RemoveSearchHistoryUseCase = mockk(relaxed = true)
    private lateinit var searchViewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        searchViewModel = SearchViewModel(
            searchMoviesUseCase = searchMoviesUseCase,
            searchTvSeriesUseCase = searchTvSeriesUseCase,
            searchActorsUseCase = searchActorsUseCase,
            addRecentViewedUseCase = addRecentViewedUseCase,
            getRecentViewedUseCase = getRecentViewedUseCase,
            getSearchHistoryUseCase = getSearchHistoryUseCase,
            clearRecentViewedUseCase = clearRecentViewedUseCase,
            clearSearchHistoryUseCase = clearSearchHistoryUseCase,
            deleteSearchItemUseCase = deleteSearchItemUseCase,
            dispatcher = testDispatcher,
        )
    }


    @Test
    fun `loadMediaByTab should load actors when ACTOR_INDEX is selected`() = runTest {
        // When
        val actorList = listOf(
            ActorUiModel(id = 1, name = "John Doe", imageUrl = "url1"),
            ActorUiModel(id = 2, name = "Jane Smith", imageUrl = "url2")
        )
        TestActorPagingSource(actorList)

        searchViewModel.updateState { it.copy(selectedTabIndex = ACTOR_INDEX) }

        // invoke private method via reflection
        val method =
            SearchViewModel::class.java.getDeclaredMethod("loadMediaByTab", String::class.java)
        method.isAccessible = true
        method.invoke(searchViewModel, "john")

        // Then
        searchViewModel.actorsPagingData.test {
            val result = awaitItem()
            Truth.assertThat(result).isNotNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `load tv shows when tab index is TV_SHOW_INDEX and query is not blank`() = runTest {
        // When

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
        // Then
        searchViewModel.actorsPagingData.test {
            val result = awaitItem()
            Truth.assertThat(result).isNotNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeRecentViewedItems() should first stop loading when start clear recent viewed item`() =
        runTest {
            // When
            searchViewModel

            // Then
            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `observeRecentViewedItems()  when start clear recent viewed item`() =
        runTest {
            // Given
            val viewedMedias = listOf(
                RecentViewedMedia(1, "https://image.com", MediaType.MOVIE, false)
            )
            coEvery { getRecentViewedUseCase.execute() } returns flowOf(viewedMedias)

            // When
            searchViewModel.observeRecentViewedItems()

            // Then
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
                                mediaType = it.mediaType.name,
                                isSaved = it.isSaved
                            )
                        }
                    )
                Truth.assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `observeRecentSearchHistory() should first stop loading when start clear recent viewed item`() =
        runTest {
            // init Then
            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `observeRecentSearchHistory()  when start clear recent viewed item`() =
        runTest {
            // Given
            val timestamp = Instant.fromEpochMilliseconds(1234567890L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val resentSearchHistories = listOf(
                SearchHistory(1, "Movie", timestamp = timestamp)
            )
            coEvery { getSearchHistoryUseCase.execute() } returns flowOf(resentSearchHistories)

            // When
            searchViewModel.observeRecentSearchHistory()

            // Then
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
        val index = SearchScreenUiState.TV_SHOW_INDEX

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
    fun `onTabSelected() should update selectedTabIndex and load media`() = runTest {
        // Given
        val initialState = searchViewModel.state.value
        Truth.assertThat(initialState.selectedTabIndex)
            .isNotEqualTo(SearchScreenUiState.TV_SHOW_INDEX)

        // When
        searchViewModel.onTabSelected(SearchScreenUiState.TV_SHOW_INDEX)

        // Then
        searchViewModel.state.test {
            val item = awaitItem()
            Truth.assertThat(item.selectedTabIndex).isEqualTo(SearchScreenUiState.TV_SHOW_INDEX)
            Truth.assertThat(item.isLoading).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Ignore("Disabled temporarily due to bug or WIP")

    @Test//
    fun `onTabSelected() should load movies when movie tap selected`() = runTest {
        // Given
        val index = SearchScreenUiState.MOVIE_INDEX
        val uiState = searchViewModel.state
        val movieName = "Movie"
        val page = 1
        val movies = listOf(SearchMovieOutput(1, movieName, "https://image.com"))
        searchViewModel.onSearchQueryChanged(movieName)
        coEvery {
            searchMoviesUseCase.execute(
                uiState.value.searchQuery,
                page = page,
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
                searchQuery = movieName,
                selectedTabIndex = index,
                isLoading = false,
                movies = movies.map { MovieUiModel(it.id, it.title, it.posterImageUrl, "") }
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Ignore("Disabled temporarily due to bug or WIP")

    @Test
    fun `onTabSelected() should load tv shows when tv show tap selected`() = runTest {
        // Given
        val index = SearchScreenUiState.TV_SHOW_INDEX
        val uiState = searchViewModel.state
        val tvShowName = "TvShow"
        val page = 1
        val tvShows = listOf(SearchTvSeriesOutput(1, tvShowName, "https://image.com"))
        searchViewModel.onSearchQueryChanged(tvShowName)
        coEvery {
            searchTvSeriesUseCase.execute(
                uiState.value.searchQuery,
                page = page,
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
                searchQuery = tvShowName,
                selectedTabIndex = index,
                isLoading = false,
                tvShows = tvShows.map { TvShowUiModel(it.id, it.title, it.posterImageUrl, "") }
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onTabSelected() should update tab and load media if new tab is selected`() = runTest {
        val index = SearchScreenUiState.TV_SHOW_INDEX

        // When
        searchViewModel.onTabSelected(index)

        // Then
        searchViewModel.state.test {
            val item = awaitItem()
            Truth.assertThat(item.selectedTabIndex).isEqualTo(index)
        }
    }

    @Ignore("Disabled temporarily due to bug or WIP")

    @Test
    fun `onTabSelected() should load actors when actor tap selected`() = runTest {
        // Given
        val index = SearchScreenUiState.ACTOR_INDEX
        val uiState = searchViewModel.state
        val actorName = "TvShow"
        val page = 1
        val actors = listOf(SearchActorOutput(1, actorName, "https://image.com"))
        searchViewModel.onSearchQueryChanged(actorName)
        coEvery {
            searchActorsUseCase.execute(
                uiState.value.searchQuery, page
            )
        } returns actors

        // When
        searchViewModel.onTabSelected(index)

        // Then
        searchViewModel.state.test {
            awaitItem()
            val item = awaitItem()
            val expected = SearchScreenUiState(
                searchQuery = actorName,
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

    @Test
    fun `onFilterApplied() should search movie when apply filters`() = runTest {
        // Given
        val filters = MediaFilters()
        val index = SearchScreenUiState.MOVIE_INDEX
        val movieName = "Movie"
        searchViewModel.onSearchQueryChanged(movieName)

        // When
        searchViewModel.onFilterApplied(filters)

        // Then
        searchViewModel.state.test {
            awaitItem()
            val item = awaitItem()
            val expected = SearchScreenUiState(
                searchQuery = movieName,
                selectedTabIndex = index,
                isLoading = false,
                filters = filters
            )
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onSearchResultMediaClicked() should doing nothing right now`() = runTest {
        // Given
        val viewed = RecentViewedUiModel(
            1, "https//image.com", MediaType.MOVIE.name, false
        )

        // When
        searchViewModel.onSearchResultMediaClicked(viewed)

        // Then
        searchViewModel.state.test {
            awaitItem()
            val item = awaitItem()
            val expected = SearchScreenUiState()
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun `onDeleteRecentSearchItem() should first stop loading when start clear recent viewed item`() =
        runTest {
            // Given
            val id = 1

            // When
            searchViewModel.onDeleteRecentSearchItem(id)

            // Then
            searchViewModel.state.test {
                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = true, error = null)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }


    @Test
    fun `onRecentSearchItemClicked() should set state search query`() = runTest {
        // Given
        val query = ""

        // When
        searchViewModel.onRecentSearchItemClicked(query)

        // Then
        searchViewModel.state.test {
            val item = awaitItem()
            val expected = SearchScreenUiState(isLoading = true, searchQuery = query)
            Truth.assertThat(item).isEqualTo(expected)
        }
    }

    @Test
    fun ` onDeleteRecentSearchItem() should update noInternetConnection = true when call api failed with NoNetworkException`() =
        runTest {
            // Given
            val id = 1
            coEvery { deleteSearchItemUseCase.execute(id) } throws NoNetworkException()

            // When
            searchViewModel.onDeleteRecentSearchItem(id)

            // Then
            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false, noInternetConnection = true)
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

    @Test
    fun `onClearRecentSearchClicked() should show Unknown error when request failed with unknown exception`() =
        runTest {
            // Given
            coEvery { clearSearchHistoryUseCase.execute() } throws Exception()

            // When
            searchViewModel.onClearRecentSearchClicked()

            // Then
            searchViewModel.state.test {
                awaitItem()

                val item = awaitItem()
                val expected = SearchScreenUiState(isLoading = false, error = "Unknown error")
                Truth.assertThat(item).isEqualTo(expected)
            }
        }

}

class TestActorPagingSource(
    private val data: List<ActorUiModel>
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
