package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ThemeUiState

interface MyAccountScreenInteractionsListener {
    fun onClickChangePassword()
    fun onClickMyTopRating()
    fun onClickWatchingHistory()
    fun onSelectLanguage(language: String)
    fun onSelectContentRestriction(contentRestriction: ContentRestrictionUiState?)
    fun onLoginButtonClick()
    fun onLogoutButtonClick()
}