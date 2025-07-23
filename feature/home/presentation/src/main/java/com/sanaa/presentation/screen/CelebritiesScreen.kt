package com.sanaa.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.components.cards.ActorCard
import com.sanaa.presentation.ui_state.ActorUiModel
import com.sanaa.presentation.ui_state.PeopleScreenEffects
import com.sanaa.presentation.ui_state.PeopleScreenInteractionListener
import com.sanaa.presentation.ui_state.PeopleScreenUiState
import com.sanaa.presentation.viewmodel.PeopleViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun CelebritiesScreen() {
    val viewModel: PeopleViewModel = viewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is PeopleScreenEffects.NavigateBack -> navController.popBackStack()
                is PeopleScreenEffects.NavigateToActorDetails -> navController.navigate("actorDetails/${effect.actorId}")
            }
        }
    }

    CelebritiesContent(
        state = state.value,
        interactionListener = viewModel
    )
}

@Composable
fun CelebritiesContent(
    state: PeopleScreenUiState,
    interactionListener: PeopleScreenInteractionListener,
) {
    NovixScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            NovixTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = com.sanaa.presentation.R.drawable.arrow_left_04),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(id = com.sanaa.presentation.R.string.people),
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = state.isLoading,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    contentAlignment = Alignment.Center
                ) { loading ->
                    if (loading) {
                        NovixLoadingIndicator()
                    } else {
                        ActorList(
                            people = state.people,
                            onItemClick = interactionListener::onActorClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActorList(
    people: List<ActorUiModel>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(people, key = { it.id }) { person ->
            ActorCard(
                actorName = person.name,
                actorImage = person.imagePainter,
                playedCharacter = person.character,
                onCardClick = { onItemClick(person.id) }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun CelebritiesScreenContentPreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        Column(
            modifier = Modifier
                .padding(vertical = 40.dp)
                .background(Theme.colors.surface)
                .fillMaxWidth()
        ) {
            CelebritiesContent(
                state = PeopleScreenUiState(
                    isLoading = false,
                    people = listOf(
                        ActorUiModel(
                            id = 1,
                            name = "Jennifer Lawrence",
                            character = null,
                            imagePainter = painterResource(R.drawable.icon_placeholder_light)
                        ),
                        ActorUiModel(
                            id = 2,
                            name = "Meryl Streep",
                            character = null,
                            imagePainter = painterResource(R.drawable.icon_placeholder_light)
                        ),
                    )
                ),
                interactionListener = object : PeopleScreenInteractionListener {
                    override fun onBackClick() {}
                    override fun onActorClick(actorId: Int) {}
                }
            )
        }
    }
}