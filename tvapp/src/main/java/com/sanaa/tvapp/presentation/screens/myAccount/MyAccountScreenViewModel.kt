package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import com.sanaa.tvapp.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import entity.User
import exceptions.NoLoggedInUserException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import repository.ContentRestriction
import repository.Language
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.LogOutUseCase
import usecase.MangeUserPreferenceUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType
import javax.inject.Inject

@HiltViewModel
class MyAccountScreenViewModel @Inject constructor(
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    val mangeUserPreference: MangeUserPreferenceUseCase,
    val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    val logOutUseCase: LogOutUseCase,
    val dispatcher: CoroutineDispatcher,
) : BaseViewModel<MyAccountScreenUiState, MyAccountScreenEffect>(
    MyAccountScreenUiState(), dispatcher
), MyAccountScreenInteractionsListener {

    init {
        fetchLanguage()
        fetchContentRestriction()
        checkUserLoggedIn()
        fetchUserData()
        loadSavedLang()
        fetchMovies()
        fetchTvShows()
    }

    override fun onSelectLanguage(language: String) {
        updateState { copy(selectedLanguage = language) }
        if (state.value.savedLanguage == state.value.selectedLanguage)
            return

        tryToExecute(
            block = ::saveLanguage,
            onSuccess = ::onSaveLanguageSuccess,
        )
    }

    override fun onSelectContentRestriction(contentRestriction: ContentRestrictionUiState?) {
        updateState { copy(selectedContentRestriction = contentRestriction) }
        if (state.value.savedContentRestriction?.name == state.value.selectedContentRestriction?.name)
            return

        tryToExecute(block = { saveContentRestriction() })
    }

    override fun onLoginButtonClick() {
        emitEffect(MyAccountScreenEffect.NavigateToLogin)
    }

    override fun onLogoutButtonClick() {
        tryToExecute(
            block = logOutUseCase::logout,
            onSuccess = {
                emitEffect(MyAccountScreenEffect.Recreate)
            }
        )
    }

    private fun fetchLanguage() {
        tryToCollect(
            block = mangeUserPreference::getLanguage,
            onCollect = ::onLoadLanguageSuccess
        )
    }

    private fun fetchContentRestriction() {
        tryToCollect(
            block = mangeUserPreference::getContentRestriction,
            onCollect = ::onLoadContentRestrictionSuccess
        )
    }

    private fun fetchMovies() {
        tryToCollect(
            block = { loadWatchedHistoryMovies() },
            onCollect = ::onCollectMovies,
        )
    }

    private fun fetchTvShows() {
        tryToCollect(
            block = { loadWatchedHistoryTvShows() },
            onCollect = ::onCollectTvShows,
        )
    }

    private fun onCollectMovies(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                watchingHistoryMovies = mediaList.map { it.toState() },
                isLoading = false
            )
        }
    }

    private fun onCollectTvShows(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                watchingHistoryTvShows = mediaList.map { it.toState() },
                isLoading = false
            )
        }
    }

    private suspend fun loadWatchedHistoryMovies(): Flow<List<MediaHistoryItem>> {
        updateState { copy(isLoading = true) }
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return flowOf(emptyList())
        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = null,
            mediaType = MediaType.MOVIE,
            username = user.first().username
        )
    }

    private suspend fun loadWatchedHistoryTvShows(): Flow<List<MediaHistoryItem>> {
        updateState { copy(isLoading = true) }
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return flowOf(emptyList())

        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = null,
            mediaType = MediaType.TV_SHOW,
            username = user.first().username
        )
    }

    private fun onSaveLanguageSuccess(newLanguage: Language) {
        emitEffect(MyAccountScreenEffect.UpdateAppLanguage(newLanguage.code))
    }

    private fun checkUserLoggedIn() {
        tryToCollect(
            block = checkIfUserIsLoggedInUseCase::isLoggedIn,
            onCollect = ::onCheckUserLoginSuccess
        )
    }

    private fun fetchUserData() {
        tryToCollect(
            block = getLoggedInUserUseCase::getLoggedInUser,
            onCollect = ::onLoadUserDataSuccess
        )
    }

    private fun onLoadLanguageSuccess(language: Language) {
        updateState { copy(selectedLanguage = language.code, savedLanguage = language.code) }
    }

    private fun onLoadUserDataSuccess(user: User) {
        updateState {
            copy(
                currentUser = UserUiState(
                    username = user.username,
                    imageUrl = user.profileImageUrl
                )
            )
        }
    }

    private fun loadSavedLang() {
        tryToCollect(
            block = mangeUserPreference::getLanguage,
            onCollect = ::onLoadLanguageSuccess
        )
    }

    private fun onCheckUserLoginSuccess(isLogged: Boolean) {
        updateState { copy(isUserLoggedIn = isLogged) }
    }

    private fun onLoadContentRestrictionSuccess(contentRestriction: ContentRestriction) {
        updateState {
            copy(
                selectedContentRestriction = ContentRestrictionUiState.valueOf(contentRestriction.name),
                savedContentRestriction = ContentRestrictionUiState.valueOf(contentRestriction.name)
            )
        }
    }

    private suspend fun saveContentRestriction() {
        ContentRestriction.entries.first {
            it.name == state.value.selectedContentRestriction?.name
        }.also {
            mangeUserPreference.setContentRestriction(it)
        }
    }

    private suspend fun saveLanguage(): Language {
        return Language.entries.first { it.code == state.value.selectedLanguage }.also {
            mangeUserPreference.setLanguage(it)
        }
    }
}