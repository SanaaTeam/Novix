package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.GenreUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toHistory
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toSeasonUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toTvShowUiModel
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvShow
import entity.User
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject


@HiltViewModel
class ShowDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val manageTvShowDetails: ManageTvShowUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TvShowDetailsScreenUiState, TvShowDetailsScreenEffects>(
    initialState = TvShowDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), TvShowScreenInteractionListener {
    val route = ScreensRoute.TvShowDetailsRoute(
        tvShowId = checkNotNull(savedStateHandle["tvShowId"])
    )

    init {
        loadSeries()
        updateUserLoginState()
    }

    override fun onActorClicked(actorId: Int) {
        emitEffect(TvShowDetailsScreenEffects.NavigateToActorScreen(actorId))
    }

    override fun onSeasonNumberClicked(seasonNumber: Int) {
        if (state.value.selectedSeason == seasonNumber) return
        tryToExecute(
            block = { fetchSeasonDetails(seasonNumber) },
            onSuccess = { updateState { copy(isLoadingEpisodes = false) } },
            onError = ::onErrorAccrue
        )
    }

    override fun onEpisodeClicked(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        emitEffect(
            TvShowDetailsScreenEffects.NavigateToEpisodeDetailsScreen(
                seriesId, seasonNumber, episodeNumber
            )
        )
    }

    override fun onPlayTrailerClicked() {
        emitEffect(TvShowDetailsScreenEffects.PlayTrailer(trailerUrl = state.value.tvShows.trailerUrl))
    }

    override fun onGenreClicked(genre: GenreUiModel) {
        emitEffect(TvShowDetailsScreenEffects.NavigateToMovieCategoriesScreen(genre))
    }

    override fun onRetryLoadDetails() {
        updateState {
            copy(
                isLoading = true,
                error = null,
                noInternetConnection = false
            )
        }

        loadSeries()
    }

    private fun loadSeries() {
        tryToExecute(
            block = ::fetchSeriesDetails,
            onSuccess = { updateState { copy(isLoading = false) } },
            onError = ::onErrorAccrue
        )
    }

    private suspend fun fetchSeriesDetails() = coroutineScope {
        updateState { copy(isLoading = true) }

        val seriesDeferred = async { manageTvShowDetails.getTvShowDetails(route.tvShowId) }
        val castDeferred = async { manageTvShowDetails.getTvShowCast(route.tvShowId) }
        val seasonDeferred = async { manageTvShowDetails.getTvShowSeasonDetails(route.tvShowId, 1) }
        val imagesDeferred = async { manageTvShowDetails.getTvShowImageUrls(route.tvShowId) }
        val trailerDeferred = async { manageTvShowDetails.getTvShowTrailer(route.tvShowId) }

        val tvShow = seriesDeferred.await()
        val cast = castDeferred.await()
        val season = seasonDeferred.await()
        val images = imagesDeferred.await()
        val trailer = trailerDeferred.await()

        addTvShowToHistory(tvShow)

        updateState {
            copy(
                tvShows = tvShow.toTvShowUiModel(trailer),
                cast = cast.map { actor -> actor.toActorUiModel() },
                season = season.toSeasonUiModel(),
                backgroundImageUrl = images.firstOrNull() ?: "",
            )
        }
    }

    private fun addTvShowToHistory(tvShow: TvShow) {
        tryToCollect(
            block = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = { user -> onCollectUserLogged(user, tvShow) }
        )
    }

    private suspend fun onCollectUserLogged(user: User, tvShow: TvShow) {
        manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
            mediaHistoryItem = tvShow.toHistory(),
            username = user.username
        )
    }

    private suspend fun fetchSeasonDetails(seasonNumber: Int) {
        updateState { copy(selectedSeason = seasonNumber, isLoadingEpisodes = true) }

        val season = manageTvShowDetails.getTvShowSeasonDetails(route.tvShowId, seasonNumber)

        updateState { copy(season = season.toSeasonUiModel()) }
    }

    override fun onRateMovieClick() {
        updateState {
            if (state.value.isUserLoggedIn)
                copy(showRateDialog = true)
            else
                copy(showLoginDialog = true)
        }
    }

    override fun onRatingChange(rating: Int) {
        updateState { copy(rating = rating) }
    }

    override fun onDismissRateDialog() {
        updateState { copy(showLoginDialog = false) }
    }

    override fun onSummitRateClick() {
        tryToExecute(block = ::submitMovieRating)
    }

    override fun onLoginButtonClick() {
        emitEffect(TvShowDetailsScreenEffects.NavigateToLogin)
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginDialog = false) }
    }

    private fun updateUserLoginState() {
        tryToCollect(
            block = checkUserLogin::isLoggedIn,
            onCollect = ::onCollectLoggedState,
        )
    }

    private fun onCollectLoggedState(loggedIn: Boolean) {
        updateState { copy(isUserLoggedIn = loggedIn) }
    }

    private suspend fun submitMovieRating() {
        val rating = state.value.rating
        val isSendRateSuccess = manageTvShowDetails.addTvShowRate(
            tvShowId = route.tvShowId,
            rating = rating.toFloat()
        )
        if (isSendRateSuccess) {
            updateState { copy(showRateDialog = false) }
        }
    }

    private fun onErrorAccrue(e: NovixAppException) {
        updateState {
            if (e is NoNetworkException) {
                copy(
                    noInternetConnection = true,
                    isLoadingEpisodes = false
                )
            } else {
                copy(
                    error = e.message,
                    noInternetConnection = false,
                    isLoadingEpisodes = false
                )
            }
        }
    }
}