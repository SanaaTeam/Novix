package com.sanaa.presentation.screen.myAccount

import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.screen.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import com.sanaa.presentation.screen.myAccount.MyAccountScreenUiState.ThemeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.User
import kotlinx.coroutines.CoroutineDispatcher
import repository.ContentRestriction
import repository.Language
import repository.Theme
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.LogOutUseCase
import usecase.MangeUserPreferenceUseCase
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class MyAccountScreenViewModel @Inject constructor(
    val mangeUserPreference: MangeUserPreferenceUseCase,
    val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    val logOutUseCase: LogOutUseCase,
    val manageSavedListsUseCase: ManageSavedListsUseCase,
    val dispatcher: CoroutineDispatcher,
) : BaseViewModel<MyAccountScreenUiState, MyAccountScreenEffect>(
    MyAccountScreenUiState(), dispatcher
), MyAccountScreenInteractionsListener {

    init {
        fetchLanguage()
        fetchContentRestriction()
        fetchTheme()
        checkUserLoggedIn()
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
        if (state.value.savedLanguage != state.value.selectedLanguage)
            tryToExecute(
                block = ::saveLanguage,
                onSuccess = ::onSaveLanguageSuccess
            )
        else updateState { copy(showChangeLanguageBottomSheet = false) }
    }

    override fun onSelectContentRestriction(contentRestriction: ContentRestrictionUiState?) {
        updateState { copy(selectedContentRestriction = contentRestriction) }
    }

    override fun onSelectTheme(theme: ThemeUiState?) {
        updateState { copy(selectedTheme = theme) }
    }

    override fun onSaveThemeClick() {
        if (state.value.savedTheme?.name != state.value.selectedTheme?.name)
            tryToExecute(
                block = ::saveTheme,
                onSuccess = ::onSaveThemeSuccess
            )
        else updateState { copy(showChangeThemeBottomSheet = false) }
    }

    override fun onSaveContentRestrictionClick() {
        if (state.value.savedContentRestriction?.name != state.value.selectedContentRestriction?.name)
            tryToExecute(
                block = { saveContentRestriction() },
                onSuccess = {
                    updateState { copy(showContentRestrictionBottomSheet = false) }
                }
            )
        else ::onSaveContentRestrictionSuccess

    }


    override fun onClickAppearance() {
        updateState { copy(showChangeThemeBottomSheet = true) }
    }

    override fun onLoginButtonClick() {
        emitEffect(MyAccountScreenEffect.NavigateToLogin)
    }

    override fun onLogoutButtonClick() {
        tryToExecute(
            block = logOutUseCase::logout,
            onSuccess = {
                emitEffect(MyAccountScreenEffect.PopBackStackToWelcomeScreen)
                clearSavedListData()
            }
        )
    }

    private fun clearSavedListData() = tryToExecute(block = manageSavedListsUseCase::clearData)


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


    private fun fetchTheme() {
        tryToCollect(
            block = mangeUserPreference::getTheme,
            onCollect = ::onLoadThemeSuccess
        )
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
        if (isLogged) fetchUserData()
        updateState {
            copy(
                isUserLoggedIn = isLogged
            )
        }

    }

    private fun onSaveContentRestrictionSuccess() {
        updateState { copy(showContentRestrictionBottomSheet = false) }
    }

    private fun onLoadContentRestrictionSuccess(contentRestriction: ContentRestriction) {
        updateState {
            copy(
                selectedContentRestriction = ContentRestrictionUiState.valueOf(contentRestriction.name),
                savedContentRestriction = ContentRestrictionUiState.valueOf(contentRestriction.name)
            )
        }
    }

    private fun onLoadThemeSuccess(theme: Theme) {
        updateState {
            copy(
                selectedTheme = ThemeUiState.valueOf(theme.name),
                savedTheme = ThemeUiState.valueOf(theme.name)
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

    private suspend fun saveTheme(): Theme {
        return Theme.entries.first { it.name == state.value.selectedTheme?.name }.also {
            mangeUserPreference.setTheme(it)
        }
    }
}