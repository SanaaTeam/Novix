package com.sanaa.presentation.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import usecase.AddRecentViewedUseCase
import usecase.ClearRecentViewedUseCase
import usecase.ClearSearchHistoryUseCase
import usecase.GetRecentViewedUseCase
import usecase.GetSearchHistoryUseCase
import usecase.RemoveSearchHistoryUseCase
import usecase.SearchActorsPagingUseCase
import usecase.SearchMoviesPagingUseCase
import usecase.SearchTvShowsPagingUseCase
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.RecentViewedMedia

class SearchViewModel(
    private val searchMoviesPagingUseCase: SearchMoviesPagingUseCase,
    private val searchTvShowsPagingUseCase: SearchTvShowsPagingUseCase,
    private val searchActorsPagingUseCase: SearchActorsPagingUseCase,
    private val addRecentViewedUseCase: AddRecentViewedUseCase,
    private val getRecentViewedUseCase: GetRecentViewedUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val deleteSearchItemUseCase: RemoveSearchHistoryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SearchScreenUiState>(SearchScreenUiState(), dispatcher),
    SearchScreenInteractionsListener {

    // Paging State Flows
    private val _moviesPagingData = MutableStateFlow<PagingData<MovieUiModel>>(PagingData.empty())
    val moviesPagingData: StateFlow<PagingData<MovieUiModel>> = _moviesPagingData

    private val _tvShowsPagingData = MutableStateFlow<PagingData<TvShowUiModel>>(PagingData.empty())
    val tvShowsPagingData: StateFlow<PagingData<TvShowUiModel>> = _tvShowsPagingData

    private val _actorsPagingData = MutableStateFlow<PagingData<ActorUiModel>>(PagingData.empty())
    val actorsPagingData: StateFlow<PagingData<ActorUiModel>> = _actorsPagingData

    init {
        observeRecentViewedItems()
        observeRecentSearchHistory()
        observeSearchQueryChanges()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueryChanges() {
        viewModelScope.launch {
            state.map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(500L)
                .collectLatest { query ->
                    if (query.isBlank()) {
                        clearSearchResults()
                    } else {
                        loadMediaByTab(query)
                    }
                }
        }
    }

    fun observeRecentViewedItems() {
        updateState {
            it.copy(
                isLoading = true, error = null, noInternetConnection = false
            )
        }

        tryToExecute(
            callee = {
                getRecentViewedUseCase.execute()
                    .map { items ->
                        items.map {
                            RecentViewedUiModel(
                                id = it.id,
                                imageUrl = it.posterImageUrl,
                                mediaType = it.mediaType.name,
                                isSaved = it.isSaved
                            )
                        }
                    }
                    .collectLatest { viewed ->
                        updateState {
                            it.copy(
                                recentViewedMedia = viewed, noInternetConnection = false
                            )
                        }
                    }
            },
            onError = ::onDataLoadError
        )
    }

    fun observeRecentSearchHistory() {
        tryToExecute(
            callee = {
                getSearchHistoryUseCase.execute()
                    .map { items ->
                        items.map {
                            RecentSearchUiModel(
                                id = it.id,
                                title = it.query
                            )
                        }
                    }
                    .collectLatest { queries ->
                        updateState {
                            it.copy(
                                recentSearchQueries = queries,
                                noInternetConnection = false
                            )
                        }
                    }
            },
            onError = ::onDataLoadError
        )
    }

    private fun clearSearchResults() {
        _moviesPagingData.value = PagingData.empty()
        _tvShowsPagingData.value = PagingData.empty()
        _actorsPagingData.value = PagingData.empty()
        updateState {
            it.copy(
                isLoading = false,
                error = null
            )
        }
    }

    private fun loadMediaByTab(query: String) {
        if (query.isBlank()) return
        when (state.value.selectedTabIndex) {
            MOVIE_INDEX -> loadMovies(query)
            TV_SHOW_INDEX -> loadTvShows(query)
            ACTOR_INDEX -> loadActors(query)
        }
    }

    private fun loadMovies(query: String) {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
        
        viewModelScope.launch {
            try {
                searchMoviesPagingUseCase(query, state.value.filters)
                    .map { pagingData ->
                        pagingData.map { searchMediaOutput ->
                            MovieUiModel(
                                id = searchMediaOutput.id,
                                title = searchMediaOutput.title,
                                imageUrl = searchMediaOutput.posterImageUrl,
                                rating = ""
                            )
                        }
                    }
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _moviesPagingData.value = pagingData
                        updateState { it.copy(isLoading = false, noInternetConnection = false) }
                    }
            } catch (e: Exception) {
                onDataLoadError(e)
            }
        }
    }

    private fun loadTvShows(query: String) {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
        
        viewModelScope.launch {
            try {
                searchTvShowsPagingUseCase(query, state.value.filters)
                    .map { pagingData ->
                        pagingData.map { searchMediaOutput ->
                            TvShowUiModel(
                                id = searchMediaOutput.id,
                                title = searchMediaOutput.title,
                                imageUrl = searchMediaOutput.posterImageUrl,
                                rating = ""
                            )
                        }
                    }
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _tvShowsPagingData.value = pagingData
                        updateState { it.copy(isLoading = false, noInternetConnection = false) }
                    }
            } catch (e: Exception) {
                onDataLoadError(e)
            }
        }
    }

    private fun loadActors(query: String) {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
        
        viewModelScope.launch {
            try {
                searchActorsPagingUseCase(query)
                    .map { pagingData ->
                        pagingData.map { searchActorOutput ->
                            ActorUiModel(
                                id = searchActorOutput.id,
                                name = searchActorOutput.name,
                                imageUrl = searchActorOutput.profileImageUrl
                            )
                        }
                    }
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _actorsPagingData.value = pagingData
                        updateState { it.copy(isLoading = false, noInternetConnection = false) }
                    }
            } catch (e: Exception) {
                onDataLoadError(e)
            }
        }
    }

    private fun onDataLoadError(e: Exception) {
        if (e is NoNetworkException)
            updateState {
                it.copy(
                    noInternetConnection = true,
                    isLoading = false,
                )
            }
        else
            updateState {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error",
                    noInternetConnection = false
                )
            }
    }

    override fun onSearchQueryChanged(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    override fun onTabSelected(index: Int) {
        updateState { it.copy(selectedTabIndex = index) }
        val searchQuery = state.value.searchQuery
        loadMediaByTab(searchQuery)
    }

    override fun onFilterApplied(filters: MediaFilters?) {
        updateState { it.copy(filters = filters) }

        val currentQuery = state.value.searchQuery
        loadMediaByTab(currentQuery)
    }

    override fun onSearchResultMediaClicked(viewed: RecentViewedUiModel) {
        tryToExecute(
            callee = {
                addRecentViewedUseCase.execute(
                    RecentViewedMedia(
                        id = viewed.id,
                        posterImageUrl = viewed.imageUrl,
                        mediaType = MediaType.valueOf(viewed.mediaType),
                        isSaved = viewed.isSaved
                    )
                )
            },
            onError = ::onDataLoadError
        )
    }

    override fun onClearRecentViewClicked() {
        tryToExecute(clearRecentViewedUseCase::execute, onSuccess = {}, onError = ::onDataLoadError)
    }

    override fun onClearRecentSearchClicked() {
        tryToExecute(
            clearSearchHistoryUseCase::execute,
            onSuccess = {},
            onError = ::onDataLoadError
        )
    }

    override fun onDeleteRecentSearchItem(id: Int) {
        updateState { it.copy(isLoading = true, error = null) }
        tryToExecute(
            callee = { deleteSearchItemUseCase.execute(id) },
            onSuccess = { updateState { it.copy(isLoading = false,noInternetConnection = false) } },
            ::onDataLoadError
        )
    }

    override fun onRecentSearchItemClicked(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    override fun onSaveIconClicked() {

    }

    companion object {
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1
        const val ACTOR_INDEX = 2
    }
}