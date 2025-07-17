package com.sanaa.presentation.screens.actors.screen

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.TopMoviesScreenRoute
import com.sanaa.presentation.screens.actors.ActorScreenEffects
import com.sanaa.presentation.screens.actors.ActorScreenUiState
import com.sanaa.presentation.screens.actors.ActorUiModel
import com.sanaa.presentation.screens.actors.ActorsScreenInteractionListener
import com.sanaa.presentation.screens.actors.componants.WavyProgressIndicator
import com.sanaa.presentation.screens.actors.ActorViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActorScreen(
    actorId: Int,
) {

    val viewModel: ActorViewModel =
        koinViewModel<ActorViewModel>(parameters = { parametersOf(actorId) })
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ActorScreenEffects.NavigateBack -> { }
//                    navController.popBackStack()

                is ActorScreenEffects.NavigateToTopMovies ->
                    navController.navigate(
                        TopMoviesScreenRoute(effect.actorId).route()
                    )
            }
        }
    }

    NovixTheme(isSystemInDarkTheme()) {

        ActorScreenContent(
//            actorId = actorId,
            state = uiState,
            listener = viewModel,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorScreenContent(
    state: ActorScreenUiState,
    modifier: Modifier = Modifier,
    listener: ActorsScreenInteractionListener
) {

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            WavyProgressIndicator()
        }
    } else {
        Log.d("state" , state.toString())
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Box {
                    ImageSlider(
                        images = state.profileImages,
                        contentDescription = "Actor photos"
                    )

                    AppTopBar(
                        leftContent = {
                            TopBarClickableIcon(
                                icon = painterResource(id = R.drawable.icon_arrow_back),
                                onClick = listener::onBackClicked
                            )
                        },
                        modifier = Modifier.padding(top = 52.dp)
                    )
                }
            }

            item {
                ActorInfoCard(
                    actor = state.actor,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-16).dp)
                )
            }

            state.actor.biography?.let {
                item {
                    OverviewSection(
                        titleResId = R.string.clear,
                        overview = state.actor.biography.orEmpty(),
                        onReadMore = { /* expand */ },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
            }

            mediaSection(
                title = "Top movie picks",
                items = state.topMovies,
                onActionClick = listener::onTopMoviesClicked
            ) { movie ->
                PosterCard(movie.imageUrl)
            }

            Log.d("ActorScreenContent", "Top series: ${state.topTvSeries}")
            mediaSection(
                title = "Top series picks",
                items = state.topTvSeries,
                onActionClick = { /* see all series */ }
            ) { series ->
                PosterCard(series.imageUrl)
            }
        }
    }

}


@Composable
private fun PosterCard(imageUrl: String?) {
    MovieSeriesPosterCard(
        modifier = Modifier
            .width(158.dp)
            .height(210.dp),
        boastImage = {
            RemoteCensoredImageViewer(
                imageUrl = imageUrl ?: "",
                modifier = Modifier,
                contentScale = ContentScale.Crop,
                blurRadius = 150,
                sfwThreshold = 0.75f,
                nsfwThreshold = 0.15f,
                contentDescription = null,
                placeholderBackgroundColor = Theme.colors.surface,
                hintText = stringResource(R.string.clear),
                textStyle = Theme.textStyle.body.small,
                iconSize = 24.dp,
            )
        },
        topLeftContent = { SaveIconChip(onClick = { /* save */ }) }
    )
}


@Composable
private fun ActorInfoCard(
    actor: ActorUiModel,
    modifier: Modifier = Modifier,
) {
    InfoSection(title = actor.name, modifier = modifier) {

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            actor.department?.let {
                Text(
                    text = it,
                    style = Theme.textStyle.label.small,
                    color = Theme.colors.body,
                )
            }

            actor.placeOfBirth?.let {
                AttributeWithDot(text = it)
            }

            actor.birthDate?.let {
                AttributeWithDot(text = it)
            }

            actor.deathDate?.let {
                AttributeWithDot(text = it)
            }
        }
    }
}

@Composable
private fun AttributeWithDot(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DotSeparator()
        IconWithText(
            iconRes = R.drawable.cancel,
            text = text,
            contentDescription = "",
            tint = Theme.colors.body
        )
    }
}


@Composable
fun SectionHeader(
    title: String, actionText: String, onActionClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = title,
            style = Theme.textStyle.label.medium,
            color = Theme.colors.body,
            modifier = Modifier.weight(1f)
        )
        TextButton(
            text = actionText, onClick = onActionClick, isLoading = false, isEnabled = true
        )
    }
}


private fun <T> LazyListScope.mediaSection(
    title: String,
    items: List<T>,
    onActionClick: () -> Unit = {},
    itemContent: @Composable (T) -> Unit,
) {
    if (items.isEmpty()) return        // skip when there is nothing to show

    // header
    item {
        SectionHeader(
            title = title,
            actionText = "All →",
            onActionClick = onActionClick
        )
    }

    // horizontal list
    item {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 24.dp
            )
        ) {
            itemsIndexed(items) { _, item -> itemContent(item) }
        }
    }
}