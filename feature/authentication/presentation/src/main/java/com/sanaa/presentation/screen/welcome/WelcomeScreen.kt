package com.sanaa.presentation.screen.welcome

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.LoginRoute
import com.sanaa.presentation.screen.welcome.components.WelcomeFooter
import com.sanaa.presentation.screen.welcome.components.WelcomeHero
import org.koin.androidx.compose.koinViewModel

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {}
) {
    val viewModel: WelcomeViewModel = koinViewModel()
    val navController = LocalNavControllerProvider.current

    BackHandler(onBack = viewModel::onExit)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                WelcomeScreenEffects.NavigateToLogin ->
                    navController.navigate(LoginRoute.route())

                WelcomeScreenEffects.ContinueAsGuest -> {
                    // TODO: navigate into your home flow
                }

                WelcomeScreenEffects.ExitApp -> {
                    (navController.context as? Activity)?.finish()
                    onExit()
                }
            }
        }
    }

    Box(modifier = modifier) {
        WelcomeContent(
            onLoginClicked = viewModel::onLoginClicked,
            onContinueClicked = viewModel::onContinueClicked,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeContent(
    onLoginClicked: () -> Unit,
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NovixScaffold(backgroundShapes = { NovixBackgroundShapes() }) {
        Column(
            modifier = modifier
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            WelcomeHero(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            WelcomeFooter(
                onLoginClicked = onLoginClicked,
                onContinueClicked = onContinueClicked
            )
        }
    }
}