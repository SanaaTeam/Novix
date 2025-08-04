package com.sanaa.presentation.screen.myAccount

import com.sanaa.presentation.user_profile_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import repository.Language
import repository.Theme
import usecase.MangeUserPreferenceUseCase
import javax.inject.Inject

@HiltViewModel
class MyAccountScreenViewModel @Inject constructor(
    val mangeUserPreference: MangeUserPreferenceUseCase,
    val dispatcher: CoroutineDispatcher,
) : BaseViewModel<MyAccountScreenUiState, MyAccountScreenEffect>(
    MyAccountScreenUiState(), dispatcher
), MyAccountScreenInteractionsListener {

    init {
        tryToExecute(
            callee = { fetchUserPreference() },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            }
        )
    }

    override fun onClickChangePassword() {
        emitEffect(MyAccountScreenEffect.NavigateToChangePasswordSetting)
    }

    override fun onClickContentRestriction() {
        updateState { it.copy(showContentRestrictionBottomSheet = true) }
    }

    override fun onClickLanguageSetting() {
        updateState { it.copy(showChangeLanguageBottomSheet = true) }
    }

    override fun onClickMyTopRating() {
        emitEffect(MyAccountScreenEffect.NavigateToMyRating)
    }

    override fun onClickWatchingHistory() {
        emitEffect(MyAccountScreenEffect.NavigateToWatchingHistory)
    }

    override fun onSelectLanguage(language: String) {
        updateState { it.copy(selectedLanguage = language) }
    }

    override fun onDismissBottomSheet() {
        updateState {
            it.copy(
                showContentRestrictionBottomSheet = false,
                showChangeLanguageBottomSheet = false
            )
        }
    }

    override fun onSaveLanguageClick() {
        tryToExecute(
            callee = {
                val newLanguage = Language.entries.first { it.code == state.value.selectedLanguage }
                mangeUserPreference.setLanguage(newLanguage)
                newLanguage
            },
            onSuccess = { newLanguage ->
                updateState { it.copy(showChangeLanguageBottomSheet = false) }
                emitEffect(MyAccountScreenEffect.UpdateAppLanguage(newLanguage.code))
            }
        )
    }

    override fun onSelectContentRestriction(contentRestriction: ContentRestrictionUiState?) {
        updateState {
            it.copy(selectedContentRestriction = contentRestriction)
        }
    }

    override fun onSelectTheme(theme: ThemeUiState?) {
        updateState {
            it.copy(selectedTheme = theme)
        }
    }

    override fun onSaveThemeClick() {
        tryToExecute(
            callee = {
                val newTheme = Theme.entries.first { it.name == state.value.selectedTheme?.name }
                mangeUserPreference.setTheme(newTheme)
                newTheme
            },
            onSuccess = { newTheme ->
                updateState { it.copy(showChangeThemeBottomSheet = false) }
                emitEffect(
                    MyAccountScreenEffect.UpdateAppTheme(
                        isDarkMode = newTheme == Theme.DARK
                    )
                )
            }
        )
    }

    override fun onSaveContentRestrictionClick() {
        tryToExecute(
            callee = {
                mangeUserPreference.setContentRestriction(
                    repository.ContentRestriction.entries.first { it.name == state.value.selectedContentRestriction?.name }
                )
            },
            onSuccess = {
                updateState { it.copy(showContentRestrictionBottomSheet = false) }
            }
        )
    }

    override fun onClickAppearance() {
        updateState { it.copy(showChangeThemeBottomSheet = true) }
    }

    private suspend fun fetchUserPreference() {
        updateState { it.copy(isLoading = true) }

        val language = mangeUserPreference.getLanguage().first()
        updateState { it.copy(selectedLanguage = language.code) }

        val contentRestriction = mangeUserPreference.getContentRestriction().first()
        updateState {
            it.copy(
                selectedContentRestriction = ContentRestrictionUiState.valueOf(
                    contentRestriction.name
                )
            )
        }
        mangeUserPreference.getTheme().collect { theme ->
            updateState {
                it.copy(
                    selectedTheme = ThemeUiState.valueOf(
                        theme.name
                    )
                )
            }
        }
    }
}