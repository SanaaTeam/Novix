package com.sanaa.presentation.screen.series

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.R
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.screen.series.components.CastComponent
import com.sanaa.presentation.screen.series.components.EpisodesContent
import com.sanaa.presentation.screen.series.components.SeasonTap
import com.sanaa.presentation.screen.series.components.SeriesTopScreen
import com.sanaa.presentation.screen.series.components.TrailerCard
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeriesScreen(
    seriesId: Int,
    viewModel: SeriesViewModel = koinViewModel(parameters = { parametersOf(seriesId) }),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    SeriesScreenContent(
        interactionListener = viewModel, state = state.value
    )

}

@Composable
fun SeriesScreenContent(
    interactionListener: SeriesScreenInteractionListener,
    state: SeriesScreenUiState
) {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        NovixScaffold(
            backgroundShapes = { NovixBackgroundShapes() },
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AppTopBar(
                    leftContent = {
                        TopBarClickableIcon(
                            icon = painterResource(R.drawable.icon_back),
                            onClick = interactionListener::onBackClicked

                        )
                    }, rightContent = {
                        TopBarClickableIcon(
                            icon = painterResource(R.drawable.icon_save),
                            onClick = interactionListener::onBackClicked
                        )
                    }, modifier = Modifier
                        .systemBarsPadding()
                        .zIndex(10f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            state = rememberScrollState()
                        )
                ) {
                    SeriesTopScreen(
                        interactionListener = interactionListener,
                        state = state,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                    Column(
                        modifier = Modifier.padding(top = 388.dp, bottom = 72.dp)
                    ) {
                        OverviewSection(
                            onReadMore = interactionListener::onReadMoreClicked,
                            titleResId = R.string.overview,
                            overview = state.series.overview,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        CastComponent(
                            cast = state.cast,
                            onActorClicked = interactionListener::onActorClicked,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        SeasonTap(
                            onClick = interactionListener::onSeasonNumberClicked,
                            seasonCounts = state.series.seasonsCount,
                            currentSeason = state.selectedSeason,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        EpisodesContent(episodes = state.season.episodes)
                    }

                }
                TrailerCard(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    trailerUrl = state.series.trailerUrl,
                )
            }
        }
    }
}
