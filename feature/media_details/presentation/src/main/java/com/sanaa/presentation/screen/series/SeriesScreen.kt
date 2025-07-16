package com.sanaa.presentation.screen.series

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.cards.ActorCard
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.screen.series.components.EpisodesContent
import com.sanaa.presentation.screen.series.components.SeasonTap
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
    interactionListener: SeriesScreenInteractionListener, state: SeriesScreenUiState
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
                    ImageSlider(
                        images = state.images,
                        modifier = Modifier.align(Alignment.TopCenter),
                        contentDescription = null
                    )
                    InfoSection(
                        title = state.series.title,
                        modifier = Modifier
                            .padding(top = 208.dp, start = 16.dp, end = 16.dp)
                            .height(158.dp)
                            .align(Alignment.TopCenter)

                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxSize()
                        ) {
                            TextButton(
                                text = stringResource(R.string.view_reviews),
                                onClick = { interactionListener.onViewReviewsClicked(state.series.id) },
                            )
                        }

                    }
                    Column(
                        modifier = Modifier.padding(top = 388.dp, bottom = 72.dp)
                    ) {
                        OverviewSection(
                            onReadMore = interactionListener::onReadMoreClicked,
                            titleResId = R.string.overview,
                            overview = state.series.overview,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )


                        Text(
                            text = stringResource(R.string.cast),
                            style = Theme.textStyle.title.medium,
                            color = Theme.colors.title,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.cast.size, key = { state.cast[it].id }) {
                                ActorCard(
                                    actorName = state.cast[it].name,
                                    playedCharacter = state.cast[it].character,
                                    actorImage = rememberAsyncImagePainter(state.cast[it].profilePath),
                                    modifier = Modifier.width(296.dp)
                                )
                            }
                        }
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
