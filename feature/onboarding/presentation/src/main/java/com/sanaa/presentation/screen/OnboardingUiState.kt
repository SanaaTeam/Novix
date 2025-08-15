package com.sanaa.presentation.screen

import com.sanaa.presentation.component.OnBoardingPageContentItem


data class OnboardingUiState(
    val pageList: List<OnBoardingPageContentItem> = emptyList(),
    val currentPageIndex: Int = 0,
    val isSkipAble: Boolean = false,
    val onboardingIsFinished :Boolean = false
)