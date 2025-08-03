package com.sanaa.presentation.screen.myAccount

import com.sanaa.presentation.user_profile_base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

class MyAccountScreenViewModel(
    val dispatcher: CoroutineDispatcher,
) : BaseViewModel<MyAccountScreenUiState, MyAccountScreenEffect>(
    MyAccountScreenUiState(), dispatcher
), MyAccountScreenInteractionsListener {
    override fun navigateChangePasswordSetting() {
        emitEffect(MyAccountScreenEffect.NavigateToChangePasswordSetting)
    }

    override fun navigateContentRestrictionSetting() {
        emitEffect(MyAccountScreenEffect.NavigateToContentRestrictionSetting)
    }

    override fun navigateLanguageSetting() {
        emitEffect(MyAccountScreenEffect.NavigateToLanguageSetting)
    }

    override fun navigateToMyRating() {
        emitEffect(MyAccountScreenEffect.NavigateToMyRating)
    }

    override fun navigateToWatchingHistory() {
        emitEffect(MyAccountScreenEffect.NavigateToWatchingHistory)
    }
}