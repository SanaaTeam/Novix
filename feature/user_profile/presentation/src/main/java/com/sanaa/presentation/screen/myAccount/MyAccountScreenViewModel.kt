package com.sanaa.presentation.screen.myAccount

import com.sanaa.presentation.user_profile_base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

class MyAccountScreenViewModel(
    val dispatcher: CoroutineDispatcher,
) : BaseViewModel<MyAccountScreenUiState, MyAccountScreenEffect>(
    MyAccountScreenUiState(), dispatcher
), MyAccountScreenInteractionsListener {
    override fun onClickChangePassword() {
        emitEffect(MyAccountScreenEffect.NavigateToChangePasswordSetting)
    }

    override fun onClickContentRestriction() {
        emitEffect(MyAccountScreenEffect.NavigateToContentRestrictionSetting)
    }

    override fun onClickLanguageSetting() {
        emitEffect(MyAccountScreenEffect.NavigateToLanguageSetting)
    }

    override fun onClickMyTopRating() {
        emitEffect(MyAccountScreenEffect.NavigateToMyRating)
    }

    override fun onClickWatchingHistory() {
        emitEffect(MyAccountScreenEffect.NavigateToWatchingHistory)
    }
}