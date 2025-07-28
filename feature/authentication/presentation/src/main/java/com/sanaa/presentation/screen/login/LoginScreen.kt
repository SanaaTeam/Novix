package com.sanaa.presentation.screen.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.presentation.navigation.ForgetPasswordRoute
import com.sanaa.presentation.navigation.HomeScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.SignUpRoute
import com.sanaa.presentation.screen.login.components.LoginActions
import com.sanaa.presentation.screen.login.components.LoginHeader
import com.sanaa.presentation.screen.login.components.LoginTopBar
import com.sanaa.presentation.screen.login.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.screen.login.components.PasswordField
import com.sanaa.presentation.screen.login.components.SignupFooter
import com.sanaa.presentation.screen.login.components.UsernameField
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current
    var snack by remember { mutableStateOf<SnackData?>(null) }

    BackHandler(onBack = viewModel::onBackClicked)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginScreenEffects.NavigateBack ->
                    navController.popBackStack()

                LoginScreenEffects.NavigateToHome -> {
                    navController.navigate(HomeScreenRoute)
                }

                LoginScreenEffects.NavigateToForgotPassword -> {
                    navController.navigate(ForgetPasswordRoute)
                }

                LoginScreenEffects.NavigateToCreateAccount -> {
                    navController.navigate(SignUpRoute)
                }

                is LoginScreenEffects.ShowError -> {
                    snack = SnackData(effect.message, isError = true)
                }

                is LoginScreenEffects.ShowSuccess -> {
                    snack = SnackData(effect.message, isError = false)
                }
            }
        }
    }

    Box(modifier = modifier) {
        LoginContent(
            state = uiState,
            listener = viewModel,
            modifier = Modifier.navigationBarsPadding()
        )

        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }
}

@Composable
fun LoginContent(
    state: LoginUiState,
    listener: LoginScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() }
    ) {
        Column(modifier = modifier.statusBarsPadding()) {
            LoginTopBar(onBackClick = listener::onBackClicked)

            Column(
                modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LoginHeader()
                Spacer(Modifier.height(40.dp))

                UsernameField(
                    value = state.username,
                    onValueChange = listener::onUsernameChanged
                )

                PasswordField(
                    value = state.password,
                    onValueChange = listener::onPasswordChanged,
                    isVisible = state.isPasswordVisible,
                    onToggleVisibility = listener::onTogglePasswordVisibility
                )

                LoginActions(
                    onLogin = listener::onLoginClicked,
                    onForgot = listener::onForgotPasswordClicked,
                    isEnabled = state.canSubmit,
                    isLoading = state.isLoading
                )

                Spacer(modifier = Modifier.weight(1f))

                SignupFooter(onCreateAccount = listener::onCreateAccountClicked)
            }
        }
    }
}