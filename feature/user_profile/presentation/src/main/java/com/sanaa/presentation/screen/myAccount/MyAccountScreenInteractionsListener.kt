package com.sanaa.presentation.screen.myAccount

interface MyAccountScreenInteractionsListener {
    fun onClickChangePassword()
    fun onClickContentRestriction()
    fun onClickLanguageSetting()
    fun onClickMyTopRating()
    fun onClickWatchingHistory()
    fun onSelectLanguage(language: String)
    fun onDismissBottomSheet()
    fun onSaveLanguageClick()
}