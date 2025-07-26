package com.sanaa.presentation.screen.celebritiesScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.components.lists.PersonList
import com.sanaa.presentation.state.PersonUiState
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun CelebritiesScreen(
    onActorClick: (Int) -> Unit,
    viewModel: CelebritiesViewModel = koinViewModel<CelebritiesViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val navController = LocalAppNavController.current
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CelebritiesScreenEffects.NavigateBack -> {
                    navController.popBackStack()
                }
                is CelebritiesScreenEffects.NavigateToActorDetails ->{
                    onActorClick(effect.actorId)
                }
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
    state: CelebritiesScreenUiState,
    interactionListener: CelebritiesScreenInteractionListener,
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
                        icon = painterResource(id = R.drawable.arrow_left_04),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(id = R.string.people),
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
                    targetState = state.isLoading to state.isNoInternetConnection,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    contentAlignment = Alignment.Center
                ) { (loading, disconnected) ->
                    when {
                        disconnected -> {
                            NetworkDisconnectionContact(TODO() )
                        }

                        loading -> {
                            NovixLoadingIndicator()
                        }

                        else -> {
                            PersonList(
                                celebrities = state.celebrities,
                                onItemClick = interactionListener::onActorClick
                            )
                        }
                    }
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        Column(
            modifier = Modifier
                .background(Theme.colors.surface)
                .fillMaxWidth()
        ) {
            CelebritiesContent(
                state = CelebritiesScreenUiState(
                    isLoading = false,
                    celebrities = listOf(
                        PersonUiState(
                            id = 1,
                            name = "Jennifer Lawrence",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 2,
                            name = "Meryl Streep",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 3,
                            name = "Scarlett Johansson",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 4,
                            name = "Emma Stone",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 5,
                            name = "Brad Pitt",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 6,
                            name = "Tom Cruise",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 7,
                            name = "Angelina Jolie",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 8,
                            name = "Brad Pitt",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 9,
                            name = "Tom Cruise",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 10,
                            name = "Angelina Jolie",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 11,
                            name = "Brad Pitt",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 12,
                            name = "Tom Cruise",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 13,
                            name = "Angelina Jolie",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 14,
                            name = "Brad Pitt",
                            character = null,
                            imageUrl = String()
                        ),
                        PersonUiState(
                            id = 15,
                            name = "Tom Cruise",
                            character = null,
                            imageUrl = String()
                        ),
                    )
                ),
                interactionListener = object : CelebritiesScreenInteractionListener {
                    override fun onBackClick() {}
                    override fun onActorClick(actorId: Int) {}
                }
            )
        }
    }
}