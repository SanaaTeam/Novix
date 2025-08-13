package com.sanaa.presentation.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.carousel.CarouselDots
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.onboarding.presentation.R
import com.sanaa.presentation.component.CircleShapeBlur
import com.sanaa.presentation.component.DialogContainer
import com.sanaa.presentation.component.OnBoardingPageContentItem

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onFinishOnBoarding: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.onboardingIsFinished) onFinishOnBoarding()

    OnBoardingScreenContent(
        state = state,
        interactionListener = viewModel,
        modifier = modifier
    )
}

@Composable
fun OnBoardingScreenContent(
    state: OnboardingUiState,
    interactionListener: OnboardingInteractionsListener,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { state.pageList.size },
        initialPage = state.currentPageIndex
    )

    LaunchedEffect(state.currentPageIndex) {
        if (pagerState.currentPage != state.currentPageIndex) {
            pagerState.animateScrollToPage(state.currentPageIndex)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { interactionListener.setCurrentPage(it) }
    }
    NovixScaffold(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {

            if (state.pageList.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp)
                        .verticalScroll(rememberScrollState()),
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            contentAlignment = Alignment.Center
                        ) {
                            CircleShapeBlur(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .height(300.dp)
                            )

                            Image(
                                painter = painterResource(state.pageList[page].imageRes),
                                contentDescription = null,
                                contentScale = ContentScale.Inside,
                                modifier = Modifier.width(244.dp)
                            )
                        }

                        DialogContainer(
                            pageContent = state.pageList[page],
                            modifier = Modifier
                        )
                    }
                }
            }

            if (pagerState.currentPage != state.pageList.lastIndex) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp)
                        .align(Alignment.TopStart),
                ) {
                    TextButton(
                        text = stringResource(id = R.string.skip),
                        onClick = interactionListener::onSkipClick,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 40.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                CarouselDots(
                    totalDots = 3,
                    selectedIndex = pagerState.currentPage,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {

                    if (pagerState.currentPage != 0)

                        OutlinedButton(
                            icon = painterResource(id = R.drawable.icon_back),
                            text = null,
                            onClick = { interactionListener.onBackClick() },
                            modifier = Modifier
                        )

                    PrimaryButton(
                        icon = painterResource(id = R.drawable.icon_right_arrow),
                        text = null,
                        onClick = { interactionListener.onNextPageClick() },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    heightDp = 800,
    widthDp = 360
)
@Preview(
    name = "Arabic Preview",
    locale = "ar"
)
@Composable
private fun Preview() {
    NovixTheme {
        OnBoardingScreenContent(
            state = OnboardingUiState(
                pageList = listOf(
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
                ),
                currentPageIndex = 1,
                isSkipAble = false
            ),
            interactionListener = object : OnboardingInteractionsListener {
                override fun onNextPageClick() {}
                override fun onSkipClick() {}
                override fun setCurrentPage(pageIndex: Int) {}
                override fun onBackClick() {}
            }
        )
    }
}