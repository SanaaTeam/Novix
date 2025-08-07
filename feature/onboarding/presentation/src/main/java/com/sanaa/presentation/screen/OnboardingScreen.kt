package com.sanaa.presentation.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
    NovixTheme (isSystemInDarkTheme()){
        NovixScaffold(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding(),
            topBar = {
                if (pagerState.currentPage != state.pageList.lastIndex) {

                    TextButton(
                        text = stringResource(id = R.string.skip),
                        onClick = interactionListener::onSkipClick,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                }
            }

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),

                contentAlignment = Alignment.Center
            ) {

                if (state.pageList.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                    ) { page ->
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Box(
                                modifier = Modifier
                                    .height( 300.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {

                                CircleShapeBlur(
                                    modifier = Modifier
                                        .matchParentSize()
                                )

                                Image(
                                    painter = painterResource(state.pageList[page].imageRes),
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    modifier = Modifier.height(182.dp)
                                )
                            }

                            DialogContainer(
                                pageContent = state.pageList[page]
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
                                icon = painterResource(id = R.drawable.icon_left_arrow),
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
}




@Preview(showBackground = true, heightDp = 800, widthDp = 360)
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
                currentPageIndex = 0,
                isSkipable = false
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