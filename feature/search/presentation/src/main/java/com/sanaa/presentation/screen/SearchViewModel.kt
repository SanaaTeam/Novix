package com.sanaa.presentation.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.base.BasePagingSource
import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.SearchScreenEffects
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
import com.sanaa.presentation.screen.state.mapper.toUiState
import entity.Actor
import entity.Movie
import entity.TvSeries
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
import usecase.history.ManageHistoryUseCase
import usecase.history.history_param.SearchHistory
import usecase.search.ManageRecentViewedUseCase
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia
import usecase.search.SearchUseCase
import usecase.search.search_param.MediaFilters
import usecase.search.search_param.MediaType

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase,
    private val manageSearchHistoryUseCase: ManageHistoryUseCase,
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

    override fun onRecentViewedMediaClicked(viewed: RecentViewedUiModel) {
        if (viewed.mediaType == MediaTypeUi.MOVIE) {
            emitEffect(SearchScreenEffects.NavigateToMovieDetails(viewed.id))
        } else {
            emitEffect(SearchScreenEffects.NavigateToTvShowDetails(viewed.id))
        }
    }

    override fun onDeleteRecentSearchItem(id: Int) {
        updateState { it.copy(isLoading = true, error = null) }
        tryToExecute(
            callee = { manageSearchHistoryUseCase.removeSearchHistory(id) },
            onSuccess = { setSuccessState() },
            onError = ::onDataLoadError
        )
    }

    override fun onRecentSearchItemClicked(query: String) {
        updateState { it.copy(searchQuery = query) }
        loadMediaByTab(query)
    }


    override fun onFilterClicked() {
        updateState { it.copy(showBottomSheet = true) }
    }

    override fun onBottomSheetDragged() {
        updateState { it.copy(showBottomSheet = false) }
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

    fun observeSearchQueryChanges() {
        tryToCollect(
            callee = ::observeSearchQueryFlow,
            onCollect = ::onSearchQueryChangedCollected,
            onError = ::onDataLoadError,
        )
    }

    fun observeRecentViewedItems() {
        setLoadingState()
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
        setLoadingState()
        tryToCollect(
            callee = { loadTvShowsOperation(query) },
            onCollect = ::onTvShowsLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadMovies(query: String) {
        setLoadingState()
        tryToCollect(
            callee = { loadMoviesOperation(query) },
            onCollect = ::onMoviesLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadActors(query: String) {
        setLoadingState()
        tryToCollect(
            callee = { loadActorsOperation(query) },
            onCollect = ::onActorsLoaded,
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

    private fun onCollectRecentViewedItems(viewed: List<RecentViewedMedia>) {
        val uiItems = viewed.map { it.toUiState() }
        updateState { it.copy(recentViewedMedia = uiItems, noInternetConnection = false) }
    }

    private suspend fun onGetRecentViewedItems(): Flow<List<RecentViewedMedia>> {
        return manageRecentViewedUseCase.getRecentViewed()
    }


    suspend fun getRecentSearchHistory(): Flow<List<SearchHistory>> {
        return manageSearchHistoryUseCase.getSearchHistory()
    }

    fun onCollectRecentSearchHistory(queries: List<SearchHistory>) {
        val uiQueries = queries.map { it.toUiState() }
        updateState { it.copy(recentSearchQueries = uiQueries, noInternetConnection = false) }
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


    private fun onMoviesLoaded(pagingData: PagingData<MovieUiModel>) {
        _moviesPagingData.value = pagingData
        setSuccessState()
    }


    private fun onTvShowsLoaded(pagingData: PagingData<TvShowUiModel>) {
        _tvShowsPagingData.value = pagingData
        setSuccessState()
    }

    private fun onActorsLoaded(pagingData: PagingData<ActorUiModel>) {
        _actorsPagingData.value = pagingData
        setSuccessState()
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


    private fun loadActorsOperation(query: String): Flow<PagingData<ActorUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createActorsPagingSource(query) },
            mapper = Actor::toUiState
        )
    }


    private fun loadTvShowsOperation(query: String): Flow<PagingData<TvShowUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createTvShowsPagingSource(query) },
            mapper = TvSeries::toUiState
        )
    }

    private fun loadMoviesOperation(query: String): Flow<PagingData<MovieUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource(query) },
            mapper = Movie::toUiState
        )
    }

    private fun createActorsPagingSource(query: String): PagingSource<Int, Actor> {
        return BasePagingSource { page ->
            searchUseCase.searchActors(query = query, page = page)
        }
    }

    private fun createTvShowsPagingSource(query: String): PagingSource<Int, TvSeries> {
        return BasePagingSource { page ->
            searchUseCase.searchTvShows(query = query, page = page, filters = state.value.filters)
        }
    }

    private fun createMoviesPagingSource(query: String): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            searchUseCase.searchMovies(query = query, page = page, filters = state.value.filters)
        }
    }


    private fun <T : Any, R : Any> createPagingFlow(
        pagingSourceFactory: () -> PagingSource<Int, T>,
        mapper: (T) -> R
    ): Flow<PagingData<R>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE, enablePlaceholders = false
            ), pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map(mapper)
        }.cachedIn(viewModelScope)

    }

    private fun setLoadingState() {
        updateState { it.copy(isLoading = true, error = null, noInternetConnection = false) }
    }

    private fun setSuccessState() {
        updateState { it.copy(isLoading = false, noInternetConnection = false) }
    }

    companion object {
        private const val PAGE_SIZE = 20
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1
        const val ACTOR_INDEX = 2
    }
}