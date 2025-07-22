package com.sanaa.presentation.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.paging.SearchActorsPagingSource
import com.sanaa.presentation.paging.SearchMoviesPagingSource
import com.sanaa.presentation.paging.SearchTvShowsPagingSource
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.SearchScreenEffects
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
import com.sanaa.presentation.screen.state.mapper.toUiState
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import search.usecase.ManageRecentViewedUseCase
import search.usecase.ManageRecentViewedUseCase.RecentViewedMedia
import search.usecase.ManageSearchHistoryUseCase
import search.usecase.SearchUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase,
    private val manageSearchHistoryUseCase: ManageSearchHistoryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SearchScreenUiState, SearchScreenEffects>(SearchScreenUiState(), dispatcher),
    SearchScreenInteractionsListener {

    private val _moviesPagingData = MutableStateFlow<PagingData<MovieUiModel>>(PagingData.empty())
    val moviesPagingData: StateFlow<PagingData<MovieUiModel>> = _moviesPagingData

    private val _tvShowsPagingData = MutableStateFlow<PagingData<TvShowUiModel>>(PagingData.empty())
    val tvShowsPagingData: StateFlow<PagingData<TvShowUiModel>> = _tvShowsPagingData

    private val _actorsPagingData = MutableStateFlow<PagingData<ActorUiModel>>(PagingData.empty())
    val actorsPagingData: StateFlow<PagingData<ActorUiModel>> = _actorsPagingData

    init {
        observeSearchQueryChanges()
        observeRecentViewedItems()
        observeRecentSearchHistory()
    }


    override fun onSearchQueryChanged(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    override fun retrySearch() {
        loadMediaByTab(state.value.searchQuery)
    }

    override fun onTabSelected(index: Int) {
        if (index == state.value.selectedTabIndex) return
        if (index == ACTOR_INDEX) {
            updateState { it.copy(isFilterButtonVisible = false) }
        } else {
            updateState { it.copy(isFilterButtonVisible = true) }
        }
        updateState { it.copy(selectedTabIndex = index) }
        val searchQuery = state.value.searchQuery
        loadMediaByTab(searchQuery)
    }

    override fun onFilterApplied(filters: MediaFilters?) {
        updateState { it.copy(filters = filters) }

        val currentQuery = state.value.searchQuery
        loadMediaByTab(currentQuery)
    }

    override fun onActorClicked(id: Int) {
        emitEffect(SearchScreenEffects.NavigateToActorDetails(id))
    }

    override fun onSearchResultMediaClicked(viewed: RecentViewedUiModel) {
        if (viewed.mediaType == MediaTypeUi.MOVIE) {
            emitEffect(SearchScreenEffects.NavigateToMovieDetails(viewed.id))
        } else {
            emitEffect(SearchScreenEffects.NavigateToTvShowDetails(viewed.id))
        }
        tryToExecute(
            callee = { addRecentViewedMedia(viewed) },
            onError = ::onDataLoadError
        )
    }


    override fun onDeleteRecentSearchItem(id: Int) {
        updateState { it.copy(isLoading = true, error = null) }
        tryToExecute(
            callee = { manageSearchHistoryUseCase.removeSearchHistory(id) },
            onSuccess = {
                updateState { it.copy(isLoading = false, noInternetConnection = false) }
            }, onError = ::onDataLoadError
        )
    }

    override fun onRecentSearchItemClicked(query: String) {
        updateState { it.copy(searchQuery = query) }
        loadMediaByTab(query)
    }

    override fun onRecentViewedMediaClicked(viewed: RecentViewedUiModel) {
        if (viewed.mediaType == MediaTypeUi.MOVIE) {
            emitEffect(SearchScreenEffects.NavigateToMovieDetails(viewed.id))
        } else {
            emitEffect(SearchScreenEffects.NavigateToTvShowDetails(viewed.id))
        }
    }

    override fun onFilterClicked() {
        updateState {
            it.copy(showBottomSheet = true)
        }
    }

    override fun onBottomSheetDragged() {
        updateState {
            it.copy(
                showBottomSheet = false
            )
        }
    }

    fun observeSearchQueryChanges() {
        tryToCollect(
            callee = ::observeSearchQueryFlow,
            onCollect = ::onSearchQueryChangedCollected,
            onError = ::onDataLoadError,
        )
    }

    fun observeRecentViewedItems() {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }

        tryToCollect(
            callee = ::onGetRecentViewedItems,
            onCollect = ::onCollectRecentViewedItems,
            onError = ::onDataLoadError
        )
    }

    fun observeRecentSearchHistory() {
        tryToCollect(
            callee = ::getRecentSearchHistory,
            onCollect = ::onCollectRecentSearchHistory,
            onError = ::onDataLoadError
        )
    }

    private fun loadTvShows(query: String) {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
        tryToCollect(
            callee = { loadTvShowsOperation(query) },
            onCollect = ::onTvShowsLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadMovies(query: String) {
        updateState {
            it.copy(
                isLoading = true, error = null, noInternetConnection = false
            )
        }
        tryToCollect(
            callee = { loadMoviesOperation(query) },
            onCollect = ::onMoviesLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadActors(query: String) {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
        tryToCollect(
            callee = { onLoadActors(query) },
            onCollect = ::onActorsLoaded,
            onError = ::onDataLoadError
        )
    }

    override fun onClearRecentViewClicked() {
        tryToExecute(
            callee = manageRecentViewedUseCase::clearRecentViewed,
            onError = ::onDataLoadError
        )
    }

    override fun onClearRecentSearchClicked() {
        tryToExecute(
            manageSearchHistoryUseCase::clearSearchHistory,
            onError = ::onDataLoadError
        )
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueryFlow(): Flow<String> {
        return state.map { it.searchQuery }.distinctUntilChanged().debounce(500L)
    }

    private fun onSearchQueryChangedCollected(query: String) {
        if (query.isBlank()) clearSearchResults() else loadMediaByTab(query)
    }


    private suspend fun onGetRecentViewedItems(): Flow<List<RecentViewedUiModel>> {
        return manageRecentViewedUseCase.getRecentViewed().toUiState()
    }

    private fun onCollectRecentViewedItems(viewed: List<RecentViewedUiModel>) {
        updateState { it.copy(recentViewedMedia = viewed, noInternetConnection = false) }
    }


    suspend fun getRecentSearchHistory(): Flow<List<RecentSearchUiModel>> {
        return manageSearchHistoryUseCase.getSearchHistory().toUiState()
    }

    fun onCollectRecentSearchHistory(queries: List<RecentSearchUiModel>) {
        updateState { it.copy(recentSearchQueries = queries, noInternetConnection = false) }
    }

    private fun clearSearchResults() {
        _moviesPagingData.value = PagingData.empty()
        _tvShowsPagingData.value = PagingData.empty()
        _actorsPagingData.value = PagingData.empty()
        updateState {
            it.copy(
                movies = emptyList(),
                tvShows = emptyList(),
                actors = emptyList(),
                isLoading = false,
                error = null
            )
        }
    }

    private fun loadMediaByTab(query: String) {
        if (query.isBlank()) return
        when (state.value.selectedTabIndex) {
            MOVIE_INDEX -> loadMovies(query.trim())
            TV_SHOW_INDEX -> loadTvShows(query.trim())
            else -> loadActors(query.trim())
        }
    }


    private fun loadMoviesOperation(query: String): Flow<PagingData<MovieUiModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE, enablePlaceholders = false
            ), pagingSourceFactory = {
                SearchMoviesPagingSource(
                    searchUseCase,
                    query = query,
                    filters = state.value.filters
                )
            }).flow.map { pagingData ->
            pagingData.map { item ->
                MovieUiModel(
                    id = item.id, title = item.title, imageUrl = item.posterImageUrl, rating = ""
                )
            }
        }.cachedIn(viewModelScope)
    }

    private fun onMoviesLoaded(pagingData: PagingData<MovieUiModel>) {
        _moviesPagingData.value = pagingData
        updateState { it.copy(isLoading = false, noInternetConnection = false) }
    }


    private fun onTvShowsLoaded(pagingData: PagingData<TvShowUiModel>) {
        _tvShowsPagingData.value = pagingData
        updateState { it.copy(isLoading = false, noInternetConnection = false) }

    }

    private fun loadTvShowsOperation(query: String): Flow<PagingData<TvShowUiModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE, enablePlaceholders = false
            ), pagingSourceFactory = {
                SearchTvShowsPagingSource(
                    searchUseCase, query = query, filters = state.value.filters
                )
            }).flow.map { pagingData ->
            pagingData.map { item ->
                TvShowUiModel(
                    id = item.id, title = item.title, imageUrl = item.posterImageUrl, rating = ""
                )
            }
        }.cachedIn(viewModelScope)
    }


    private fun onActorsLoaded(pagingData: PagingData<ActorUiModel>) {
        _actorsPagingData.value = pagingData
        updateState { it.copy(isLoading = false, noInternetConnection = false) }
    }

    private fun onLoadActors(query: String): Flow<PagingData<ActorUiModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE, enablePlaceholders = false
            ), pagingSourceFactory = {
                SearchActorsPagingSource(searchUseCase, query = query)
            }).flow.map { pagingData ->
            pagingData.map { searchActorOutput ->
                ActorUiModel(
                    id = searchActorOutput.id,
                    name = searchActorOutput.name,
                    imageUrl = searchActorOutput.profileImageUrl
                )
            }
        }.cachedIn(viewModelScope)
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) updateState {
            it.copy(
                noInternetConnection = true,
                isLoading = false,
            )
        }
        else updateState {
            it.copy(
                isLoading = false,
                error = e.message ?: "Unknown error",
                noInternetConnection = false
            )
        }
    }


    private suspend fun addRecentViewedMedia(viewed: RecentViewedUiModel) {
        manageRecentViewedUseCase.addRecentViewed(
            RecentViewedMedia(
                id = viewed.id,
                posterImageUrl = viewed.imageUrl,
                mediaType = MediaType.valueOf(viewed.mediaType.name),
                isSaved = viewed.isSaved
            )
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1
        const val ACTOR_INDEX = 2
    }
}