package com.sanaa.presentation.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.screen.componants.SnackData
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.SearchScreenEffects
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
import com.sanaa.presentation.screen.state.mapper.toUiState
import com.sanaa.presentation.searchBase.BasePagingSource
import com.sanaa.presentation.searchBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Actor
import entity.Movie
import entity.TvShow
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import repository.ContentRestriction
import repository.Theme
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.MangeUserPreferenceUseCase
import usecase.history.ManageHistoryUseCase
import usecase.history.history_param.SearchHistory
import usecase.search.ManageRecentViewedUseCase
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia
import usecase.search.SearchUseCase
import usecase.search.search_param.MediaType
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase,
    private val manageSearchHistoryUseCase: ManageHistoryUseCase,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val mangeUserPreferenceUseCase: MangeUserPreferenceUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SearchScreenUiState, SearchScreenEffects>(
    SearchScreenUiState(),
    dispatcher
), SearchScreenInteractionsListener {

    init {
        observeSearchQueryChanges()
        observeRecentViewedItems()
        observeRecentSearchHistory()
        observeSelectedTheme()
        observeContentRestriction()
        getUserState()
    }

    override fun onSearchQueryChanged(query: String) {
        updateState { copy(searchQuery = query) }
    }

    override fun retrySearch() {
        loadMediaByTab(state.value.searchQuery)
    }

    override fun onTabSelected(index: Int) {
        if (index == state.value.selectedTabIndex) return
        updateState { copy(selectedTabIndex = index) }
        val searchQuery = state.value.searchQuery
        loadMediaByTab(searchQuery)
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
            block = { addRecentViewedMedia(viewed) },
            onError = ::onDataLoadError
        )
    }

    override fun onLoginButtonClick() {
        updateState { copy(showLoginBottomSheet = false) }
        emitEffect(SearchScreenEffects.NavigateToLogin)
    }


    override fun onSaveIconClick(media: MovieUiModel) {
        if (!state.value.isUserLoggedIn) {
            updateState { copy(showLoginBottomSheet = true) }
            return
        }
        updateState {
            copy(
                showSaveToListBottomSheet = true,
                selectedMediaToSave = media
            )
        }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false, selectedMediaToSave = null) }
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }

    override fun onSnackBarDismiss() {
        updateState {
            copy(snackBarData = null)
        }
    }

    override fun onSaveToListSuccess() {
        updateState {
            copy(
                snackBarData = SnackData(
                    message = stringProvider.addToListSuccess,
                    isError = false
                )
            )
        }
    }

    override fun onSaveToListFailure() {
        updateState {
            copy(
                snackBarData = SnackData(
                    message = stringProvider.addToListFailed,
                    isError = true
                )
            )
        }
    }

    override fun onRecentViewedMediaClicked(viewed: RecentViewedUiModel) {
        if (viewed.mediaType == MediaTypeUi.MOVIE) {
            emitEffect(SearchScreenEffects.NavigateToMovieDetails(viewed.id))
        } else {
            emitEffect(SearchScreenEffects.NavigateToTvShowDetails(viewed.id))
        }
    }

    override fun onDeleteRecentSearchItem(id: Int) {
        updateState { copy(isLoading = true, error = null) }
        tryToExecute(
            block = { manageSearchHistoryUseCase.removeSearchHistory(id) },
            onSuccess = { setSuccessState() },
            onError = ::onDataLoadError
        )
    }

    override fun onRecentSearchItemClicked(query: String) {
        updateState { copy(searchQuery = query) }
        loadMediaByTab(query)
    }


    override fun onLoginBottomSheetDismiss() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onClearRecentViewClicked() {
        tryToExecute(
            block = manageRecentViewedUseCase::clearRecentViewed,
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
            block = ::observeSearchQueryFlow,
            onCollect = ::onSearchQueryChangedCollected,
            onError = ::onDataLoadError,
        )
    }

    fun observeRecentViewedItems() {
        setLoadingState()
        tryToCollect(
            block = ::onGetRecentViewedItems,
            onCollect = ::onCollectRecentViewedItems,
            onError = ::onDataLoadError
        )
    }

    fun observeRecentSearchHistory() {
        tryToCollect(
            block = ::getRecentSearchHistory,
            onCollect = ::onCollectRecentSearchHistory,
            onError = ::onDataLoadError
        )
    }

    private fun loadTvShows(query: String) {
        setLoadingState()
        tryToCollect(
            block = { loadTvShowsOperation(query) },
            onCollect = ::onTvShowsLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadMovies(query: String) {
        setLoadingState()
        tryToCollect(
            block = { loadMoviesOperation(query) },
            onCollect = ::onMoviesLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadActors(query: String) {
        setLoadingState()
        tryToCollect(
            block = { loadActorsOperation(query) },
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
        updateState { copy(recentViewedMedia = uiItems, noInternetConnection = false) }
    }

    private suspend fun onGetRecentViewedItems(): Flow<List<RecentViewedMedia>> {
        return manageRecentViewedUseCase.getRecentViewed()
    }

    suspend fun getRecentSearchHistory(): Flow<List<SearchHistory>> {
        return manageSearchHistoryUseCase.getSearchHistory()
    }

    fun onCollectRecentSearchHistory(queries: List<SearchHistory>) {
        val uiQueries = queries.map { it.toUiState() }
        updateState { copy(recentSearchQueries = uiQueries, noInternetConnection = false) }
    }

    private fun clearSearchResults() {
        updateState {
            copy(
                movies = flowOf(PagingData.empty()),
                tvShows = flowOf(PagingData.empty()),
                actors = flowOf(PagingData.empty()),
                isLoading = false,
                error = null,
                noInternetConnection = false
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
        updateState { copy(movies = flowOf(pagingData)) }
        setSuccessState()
    }

    private fun onTvShowsLoaded(pagingData: PagingData<TvShowUiModel>) {
        updateState { copy(tvShows = flowOf(pagingData)) }
        setSuccessState()
    }

    private fun onActorsLoaded(pagingData: PagingData<ActorUiModel>) {
        updateState { copy(actors = flowOf(pagingData)) }
        setSuccessState()
    }

    private fun onDataLoadError(e: Throwable) {
        updateState {
            if (e is NoNetworkException) {
                copy(
                    noInternetConnection = true,
                    isLoading = false,
                    error = null
                )
            } else {
                val errorMessage = e.message ?: "An unexpected error occurred."
                copy(
                    isLoading = false,
                    error = errorMessage,
                    noInternetConnection = false
                )
            }
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
            mapper = TvShow::toUiState
        )
    }

    private fun loadMoviesOperation(query: String): Flow<PagingData<MovieUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource(query) },
            mapper = Movie::toUiState
        ).cachedIn(viewModelScope)
    }

    fun createActorsPagingSource(query: String): PagingSource<Int, Actor> {
        return BasePagingSource { page ->
            searchUseCase.searchActors(query = query, page = page)
        }
    }

    fun createTvShowsPagingSource(query: String): PagingSource<Int, TvShow> {
        return BasePagingSource { page ->
            searchUseCase.searchTvShows(query = query, page = page)
        }
    }

    fun createMoviesPagingSource(query: String): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            searchUseCase.searchMovies(
                query = query,
                page = page,
            )
        }
    }

    fun <T : Any, R : Any> createPagingFlow(
        pagingSourceFactory: () -> PagingSource<Int, T>,
        mapper: (T) -> R,
    ): Flow<PagingData<R>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 4
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map(mapper)
        }.cachedIn(viewModelScope)
    }

    private fun setLoadingState() {
        updateState { copy(isLoading = true, error = null, noInternetConnection = false) }
    }

    private fun setSuccessState() {
        updateState { copy(isLoading = false, noInternetConnection = false) }
    }

    private fun observeSelectedTheme() {
        tryToCollect(
            block = mangeUserPreferenceUseCase::getTheme,
            onCollect = { isDarkMode -> updateState { copy(isDarkMode = isDarkMode == Theme.DARK) } },
            onError = ::onDataLoadError
        )

    }

    private fun observeContentRestriction() {
        tryToCollect(
            block = mangeUserPreferenceUseCase::getContentRestriction,
            onCollect = ::onCollectContentRestriction,
        )
    }

    private fun onCollectContentRestriction(contentRestriction: ContentRestriction) {
        updateState {
            copy(
                safeContentThreshold =
                    when (contentRestriction) {
                        ContentRestriction.RESTRICTED -> STRICT_CONTENT_THRESHOLD
                        ContentRestriction.MODERATE_RESTRICTION -> MODERATE_CONTENT_THRESHOLD
                        ContentRestriction.UNRESTRICTED -> UNRESTRICTED_CONTENT_THRESHOLD
                    }
            )
        }
    }

    private fun getUserState() {
        tryToCollect(
            block = { checkUserLogin.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(isUserLoggedIn = isLogged) }
    }

    private companion object {
        private const val PAGE_SIZE = 20
        const val MOVIE_INDEX = 0
        const val TV_SHOW_INDEX = 1

        const val STRICT_CONTENT_THRESHOLD = 0.9f
        const val MODERATE_CONTENT_THRESHOLD = 0.5f
        const val UNRESTRICTED_CONTENT_THRESHOLD = 0.0f
    }
}