package com.sanaa.presentation.screen.welcome

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.LoginRoute
import com.sanaa.presentation.screen.login.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.screen.welcome.components.WelcomeFooter
import com.sanaa.presentation.screen.welcome.components.WelcomeSection


@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {
    val navController = LocalNavControllerProvider.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    BackHandler(onBack = viewModel::onExit)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                WelcomeScreenEffects.NavigateToLogin -> {
                    navController.navigate(LoginRoute)
                }

                WelcomeScreenEffects.ReturnGuestResultCode -> {
                    (navController.context as? Activity)?.finish()
                }

                WelcomeScreenEffects.ExitApp -> {
                    (navController.context as? Activity)?.finish()
                }
            }
        }
    }

    Box(modifier = modifier) {
        WelcomeContent(
            state = uiState,
            interactionListener = viewModel
        )
    }
}

@Composable
fun WelcomeContent(
    state: WelcomeScreenUiState,
    interactionListener: WelcomeScreenInteractionListener
) {
    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss
            )
        }
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            WelcomeSection(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            WelcomeFooter(
                onLoginClicked = interactionListener::onLoginClicked,
                onContinueClicked = interactionListener::onContinueClicked
            )
        }
    }
}