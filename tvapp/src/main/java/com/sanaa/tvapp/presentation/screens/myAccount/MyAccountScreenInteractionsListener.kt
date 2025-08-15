package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ThemeUiState

interface MyAccountScreenInteractionsListener {
    fun onClickChangePassword()
    fun onClickContentRestriction()
    fun onClickLanguageSetting()
    fun onClickMyTopRating()
    fun onClickWatchingHistory()
    fun onSelectLanguage(language: String)
    fun onDismissBottomSheet()
    fun onSaveLanguageClick()
    fun onSelectContentRestriction(contentRestriction: ContentRestrictionUiState?)
    fun onSelectTheme(theme: ThemeUiState?)
    fun onSaveThemeClick()
    fun onSaveContentRestrictionClick()
    fun onClickAppearance()
    fun onLoginButtonClick()
    fun onLogoutButtonClick()
}