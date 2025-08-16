package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.User
import kotlinx.coroutines.CoroutineDispatcher
import repository.ContentRestriction
import repository.Language
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.LogOutUseCase
import usecase.MangeUserPreferenceUseCase
import javax.inject.Inject

@HiltViewModel
class MyAccountScreenViewModel @Inject constructor(
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
    }

    override fun onClickChangePassword() {
        emitEffect(MyAccountScreenEffect.NavigateToChangePasswordSetting)
    }

    override fun onClickMyTopRating() {
        emitEffect(MyAccountScreenEffect.NavigateToMyRating)
    }

    override fun onClickWatchingHistory() {
        emitEffect(MyAccountScreenEffect.NavigateToWatchingHistory)
    }

    override fun onSelectLanguage(language: String) {
        updateState { copy(selectedLanguage = language) }
        if (state.value.savedLanguage == state.value.selectedLanguage)
            return

        tryToExecute(
            block = ::saveLanguage,
            onSuccess = ::onSaveLanguageSuccess
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
                emitEffect(MyAccountScreenEffect.PopBackStackToWelcomeScreen)
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