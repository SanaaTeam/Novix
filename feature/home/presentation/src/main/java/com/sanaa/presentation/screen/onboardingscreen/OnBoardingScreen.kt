package com.sanaa.presentation.screen.onboardingscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController


@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    state: OnBoardingScreenUiState,
    listener: OnBoardingScreenInteractionListener
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current

    NovixScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            TopBar(
                rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.ic_skip),
                        onClick = viewModel::onSkipClick
                    )
                },
                screenTitle = "",
                modifier = Modifier.systemBarsPadding()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = state.value.currentPage,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    contentAlignment = Alignment.Center
                ) { pageIndex ->
                    val page = state.value.pages[pageIndex]
                    OnboardingPageContent(page = page)
                }
            }

            OnboardingFooter(
                currentPage = state.value.currentPage,
                totalPages = state.value.pages.size,
                onNextClick = viewModel::onNextClick
            )
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnBoardingPageContentItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier.size(240.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        CustomText(
            text = stringResource(id = page.title),
            style = Theme.textStyle.title.large,
            color = Theme.colors.title,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = stringResource(id = page.description),
            style = Theme.textStyle.body.medium,
            color = Theme.colors.body,
        )
    }
}
@Composable
private fun OnBoardingPage(item: OnBoardingPageContentItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = stringResource(id = item.title),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title
        )
        Text(
            text = stringResource(id = item.description),
            style = Theme.textStyle.body.medium,
            color = Theme.colors.body
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOnBoardingScreen() {
    val samplePages = listOf(
        OnBoardingPageContentItem(
            title = R.string.onboarding_title_1,
            description = R.string.onboarding_desc_1,
            imageRes = R.drawable.onboarding_image_1
        ),
        OnBoardingPageContentItem(
            title = R.string.onboarding_title_2,
            description = R.string.onboarding_desc_2,
            imageRes = R.drawable.onboarding_image_2
        )
    )

    OnboardingScreen(
        state = OnBoardingScreenUiState(pages = samplePages),
        listener = object : OnBoardingScreenInteractionListener {
            override fun onNextPage(index: Int) {}
            override fun onSkip() {}
        }
    )
}