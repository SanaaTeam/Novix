package com.sanaa.presentation.screen

import androidx.lifecycle.ViewModel
import com.sanaa.feature.onboarding.presentation.R
import com.sanaa.presentation.component.OnBoardingPageContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class OnboardingViewModel : ViewModel(), OnboardingInteractionsListener {

    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    init {
        loadOnBoardingPages()
    }

    private fun loadOnBoardingPages() {

            updateState {
                copy(
                    pageList = getOnBoardingPageContent(),
                    currentPageIndex = 0
                )
            }
        }

    override fun onNextPageClick() {
        val currentIndex = state.value.currentPageIndex
        val lastIndex = state.value.pageList.lastIndex

        if (currentIndex == lastIndex) {
            onSkipClick()
        } else {
            updateState { copy(currentPageIndex = currentIndex + 1) }
        }
    }

    override fun onBackClick() {
        val currentIndex = state.value.currentPageIndex

        if (currentIndex > 0) {
            updateState { copy(currentPageIndex = currentIndex - 1) }
        }
    }

    override fun onSkipClick() {
        updateState { copy(isSkipAble = true, onboardingIsFinished = true) }
    }

    override fun setCurrentPage(pageIndex: Int) {
        updateState { copy(currentPageIndex = pageIndex) }
    }

    private fun getOnBoardingPageContent(): List<OnBoardingPageContentItem> {
        return listOf(
            OnBoardingPageContentItem(
                title = R.string.onboarding_title_0,
                description = R.string.onboarding_desc_0,
                imageRes = R.drawable.onboarding_1
            ),
            OnBoardingPageContentItem(
                title = R.string.onboarding_title_1,
                description = R.string.onboarding_desc_1,
                imageRes = R.drawable.onboarding_2
            ),
            OnBoardingPageContentItem(
                title = R.string.onboarding_title_2,
                description = R.string.onboarding_desc_2,
                imageRes = R.drawable.onboarding_3
            )
        )
    }

    private fun updateState(update: OnboardingUiState.() -> OnboardingUiState) {
        _state.value = update(_state.value)
    }
}