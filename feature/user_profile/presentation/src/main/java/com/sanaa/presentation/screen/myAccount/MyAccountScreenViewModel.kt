package com.sanaa.presentation.screen.myAccount

import com.sanaa.presentation.user_profile_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import repository.Language
import repository.UserPreference
import javax.inject.Inject

@HiltViewModel
class MyAccountScreenViewModel @Inject constructor(
    val userPreference: UserPreference,
    val dispatcher: CoroutineDispatcher,
) : BaseViewModel<MyAccountScreenUiState, MyAccountScreenEffect>(
    MyAccountScreenUiState(), dispatcher
), MyAccountScreenInteractionsListener {

    init {
        fetchUserPreference()
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
                userPreference.setLanguage(
                    Language.entries.first { it.code == state.value.selectedLanguage }
                )
            },
            onError = {},
            onSuccess = {}
        )
    }

    private fun fetchUserPreference() {
        tryToCollect(
            callee = { userPreference.getLanguage() },
            onCollect = { language ->
                updateState { it.copy(selectedLanguage = language.code) }
            },
            onError = {},
        )
    }
}