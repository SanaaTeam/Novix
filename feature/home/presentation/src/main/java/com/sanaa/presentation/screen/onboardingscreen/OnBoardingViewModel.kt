package com.sanaa.presentation.screen.onboardingscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanaa.feature.home.presentation.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class OnBoardingViewModel : ViewModel(), OnBoardingScreenInteractionListener {

    private val _state = MutableStateFlow(OnBoardingScreenUiState())
    val state: StateFlow<OnBoardingScreenUiState> = _state

    init {
        _state.value = OnBoardingScreenUiState(
            pages = listOf(
                OnBoardingPageContentItem(
                    title = R.string.onboarding_title_1,
                    description = R.string.onboarding_desc_1,
                    imageRes = R.drawable.onboarding_image_1
                ),
                OnBoardingPageContentItem(
                    title = R.string.onboarding_title_2,
                    description = R.string.onboarding_desc_2,
                    imageRes = R.drawable.onboarding_image_2
                ),
                OnBoardingPageContentItem(
                    title = R.string.onboarding_title_3,
                    description = R.string.onboarding_desc_3,
                    imageRes = R.drawable.onboarding_image_3
                )
            )
        )
    }
    override fun onNextPage(index: Int) {
    }

    override fun onSkip() {
        viewModelScope.launch {
            OnBoardingPreferences.markAsShown(TODO())
        }
    }
}