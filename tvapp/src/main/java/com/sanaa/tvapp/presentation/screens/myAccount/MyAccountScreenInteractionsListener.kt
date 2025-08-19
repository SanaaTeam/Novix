package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ContentRestrictionUiState

interface MyAccountScreenInteractionsListener {
    fun onSelectLanguage(language: String)
    fun onSelectContentRestriction(contentRestriction: ContentRestrictionUiState?)
    fun onLoginButtonClick()
    fun onLogoutButtonClick()
}