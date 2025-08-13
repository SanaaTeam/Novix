package com.sanaa.presentation.screen

import com.sanaa.feature.onboarding.presentation.R
import com.sanaa.presentation.component.OnBoardingPageContentItem
import com.sanaa.presentation.onbordingBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@HiltViewModel
class OnboardingViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<OnboardingUiState>(OnboardingUiState(), dispatcher),
    OnboardingInteractionsListener {

    init {
        loadOnBoardingPages()
    }

    private fun loadOnBoardingPages() {
        tryToExecute(
            callee = {
                updateState {
                    copy(
                        pageList = getOnBoardingPageContent(),
                        currentPageIndex = 0
                    )
                }
            }
        )
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
}