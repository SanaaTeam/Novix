package com.sanaa.presentation.screen

interface OnboardingInteractionsListener {
    fun onNextPageClick()
    fun onSkipClick()
    fun onBackClick()
    fun setCurrentPage(pageIndex: Int)
}
