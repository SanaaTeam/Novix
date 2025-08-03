package com.sanaa.presentation.screen.myRating

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.myRating.component.RatedMediaListSectionContent

@Composable
fun MyRatingScreen(
    modifier: Modifier = Modifier,
    viewModel: MyRatingScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val appContext = LocalContext.current.applicationContext

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MyRatingScreenEffect.NavigateBack -> {
//                    navController.popBackStack()
                }
                is MyRatingScreenEffect.ShowMessage -> {
                }

                is MyRatingScreenEffect.NavigateToMediaDetails -> TODO()
            }
        }
    }

    NovixTheme(isSystemInDarkTheme()) {
        MyRatingScreenContent(
            title = stringResource(R.string.my_rating),
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
        )
    }
}

@Composable
fun MyRatingScreenContent(
    title: String,
    state: MyRatingScreenUiState,
    interactionListener: MyRatingScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = Theme.colors.surface),
    ) {

        TopBar(
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = R.drawable.icon_back),
                    onClick = interactionListener::onBackClick
                )
            },
            screenTitle = title,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(bottom = 12.dp)
        )

        RatedMediaListSectionContent(
            state = state,
            onTabSelected = interactionListener::onTabSelected,
            onDeleteRatingClick = interactionListener::onDeleteIconClick,
        )
    }
}