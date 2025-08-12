package com.sanaa.presentation.screen.myAccount

import com.sanaa.presentation.profileBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import repository.ContentRestriction
import repository.Language
import repository.Theme
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
        tryToExecute(
            callee = { fetchUserPreference() },
            onSuccess = { updateState { copy(isLoading = false) } }
        )
        checkUserLoggedIn()
        fetchUserData()
        loadSavedLang()
    }

    override fun onClickChangePassword() {
        emitEffect(MyAccountScreenEffect.NavigateToChangePasswordSetting)
    }

    override fun onClickContentRestriction() {
        updateState { copy(showContentRestrictionBottomSheet = true) }
    }

    override fun onClickLanguageSetting() {
        updateState { copy(showChangeLanguageBottomSheet = true) }
    }

    override fun onClickMyTopRating() {
        emitEffect(MyAccountScreenEffect.NavigateToMyRating)
    }

    override fun onClickWatchingHistory() {
        emitEffect(MyAccountScreenEffect.NavigateToWatchingHistory)
    }

    override fun onSelectLanguage(language: String) {
        updateState { copy(selectedLanguage = language) }
    }

    override fun onDismissBottomSheet() {
        updateState {
            copy(
                showContentRestrictionBottomSheet = false,
                showChangeLanguageBottomSheet = false,
                showChangeThemeBottomSheet = false
            )
        }
    }

    override fun onSaveLanguageClick() {
        val previousLanguage = runBlocking { mangeUserPreference.getLanguage().first() }
        if (previousLanguage.code != state.value.selectedLanguage)
            tryToExecute(
                callee = { saveLanguage() },
                onSuccess = ::onSaveLanguageSuccess
            )
        else updateState { copy(showChangeLanguageBottomSheet = false) }
    }

    private suspend fun saveLanguage(): Language {
        val newLanguage = Language.entries.first { it.code == state.value.selectedLanguage }
        mangeUserPreference.setLanguage(newLanguage)
        return newLanguage
    }

    override fun onSelectContentRestriction(contentRestriction: ContentRestrictionUiState?) {
        updateState { copy(selectedContentRestriction = contentRestriction) }
    }

    override fun onSelectTheme(theme: ThemeUiState?) {
        updateState { copy(selectedTheme = theme) }
    }

    override fun onSaveThemeClick() {
        val previousTheme = runBlocking { mangeUserPreference.getTheme().first() }
        if (previousTheme.name != state.value.selectedTheme?.name)
            tryToExecute(
                callee = { saveTheme() },
                onSuccess = ::onSaveThemeSuccess
            )
        else updateState { copy(showChangeThemeBottomSheet = false) }
    }

    private suspend fun saveTheme(): Theme {
        val newTheme = Theme.entries.first { it.name == state.value.selectedTheme?.name }
        mangeUserPreference.setTheme(newTheme)
        return newTheme
    }

    override fun onSaveContentRestrictionClick() {
        val previousContentRestriction =
            runBlocking { mangeUserPreference.getContentRestriction().first() }
        if (previousContentRestriction.name != state.value.selectedContentRestriction?.name)
            tryToExecute(
                callee = { saveContentRestriction() },
                onSuccess = {
                    updateState { copy(showContentRestrictionBottomSheet = false) }
                }
            )
        else updateState { copy(showContentRestrictionBottomSheet = false) }
    }

    private suspend fun saveContentRestriction() {
        val newRestriction = ContentRestriction.entries.first {
            it.name == state.value.selectedContentRestriction?.name
        }
        mangeUserPreference.setContentRestriction(newRestriction)
    }

    override fun onClickAppearance() {
        updateState { copy(showChangeThemeBottomSheet = true) }
    }

    override fun onLoginButtonClick() {
        emitEffect(MyAccountScreenEffect.NavigateToLogin)
    }

    override fun onLogoutButtonClick() {
        tryToExecute(
            callee = {
                logOutUseCase.logout()
            },
            onSuccess = {
                emitEffect(MyAccountScreenEffect.PopBackStackToWelcomeScreen)
            }
        )
    }


    private suspend fun fetchUserPreference() {
        updateState { copy(isLoading = true) }

        fetchLanguage()
        fetchContentRestriction()
        fetchTheme()
    }

    private suspend fun fetchLanguage() {
        val language = mangeUserPreference.getLanguage().first()
        updateState { copy(selectedLanguage = language.code) }
    }

    private suspend fun fetchContentRestriction() {
        val contentRestriction = mangeUserPreference.getContentRestriction().first()
        updateState {
            copy(
                selectedContentRestriction = ContentRestrictionUiState.valueOf(contentRestriction.name)
            )
        }
    }

    private suspend fun fetchTheme() {
        mangeUserPreference.getTheme().collect { theme ->
            updateState {
                copy(selectedTheme = ThemeUiState.valueOf(theme.name))
            }
        }
    }

    private fun onSaveThemeSuccess(newTheme: Theme) {
        updateState { copy(showChangeThemeBottomSheet = false) }
        emitEffect(
            MyAccountScreenEffect.UpdateAppTheme(
                isDarkMode = newTheme == Theme.DARK
            )
        )
    }

    private fun onSaveLanguageSuccess(newLanguage: Language) {
        updateState { copy(showChangeLanguageBottomSheet = false) }
        emitEffect(MyAccountScreenEffect.UpdateAppLanguage(newLanguage.code))
    }

    private fun checkUserLoggedIn() {
        tryToCollect(
            callee = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(isUserLoggedIn = isLogged) }
    }

    private fun fetchUserData() {
        tryToCollect(
            callee = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = ::onLoadUserDataSuccess
        )
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
            callee = { mangeUserPreference.getLanguage() },
            onCollect = { saveLanguage ->
                updateState { copy(savedLanguage = saveLanguage.code) }
            }
        )
    }
}