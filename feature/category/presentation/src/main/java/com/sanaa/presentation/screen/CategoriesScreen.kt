package com.sanaa.presentation.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.tab.Tab
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.feature.category.presentation.R
import com.sanaa.presentation.api.CategoryApiEntryPoint
import com.sanaa.presentation.screen.compnents.CategoriesGrid
import dagger.hilt.android.EntryPointAccessors

@Composable
fun CategoriesScreen(
    viewModel: CategoriesScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val detailsApi = EntryPointAccessors.fromApplication(
        context,
        CategoryApiEntryPoint::class.java
    ).detailsApi()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CategoriesScreenEffects.NavigateToMovieGenreDetails -> {
                    detailsApi.navigateToMovieGenreDetails(
                        context,
                        effect.genreId,
                        effect.genreName
                    )
                }

                is CategoriesScreenEffects.NavigateToTvGenreDetails -> {
                    detailsApi.navigateToTvGenreDetails(
                        context,
                        effect.genreId,
                        effect.genreName
                    )
                }
            }
        }
    }


    CategoriesScreen(
        state = state.value,
        interactionListener = viewModel
    )
}


@Composable
private fun CategoriesScreen(
    state: CategoriesScreenUiState,
    interactionListener: CategoriesScreenInteractionListener
) {
    NovixScaffold(
        topBar = {
            TopBar(
                screenTitle = stringResource(id = R.string.categories),
                modifier = Modifier.statusBarsPadding(),
            )
        }
    ) {
        val tabs = listOf(
            stringResource(R.string.movies),
            stringResource(R.string.tv_shows)
        )
        Column(
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Tab(
                categories = tabs,
                selectedIndex = state.selectedTabIndex,
                onCategorySelected = interactionListener::onTabChanged,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Crossfade(
                targetState = state.isLoading,

                ) { isLoading ->
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }

                } else {
                    CategoriesGrid(
                        categories = if (state.selectedTabIndex ==
                            CategoriesScreenUiState.MOVIE_TAB_INDEX
                        )
                            state.movieCategories
                        else
                            state.tvCategories,
                        onCategoryClick = interactionListener::onGenreClicked
                    )
                }
            }
        }
    }
}