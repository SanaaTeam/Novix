package com.sanaa.presentation.screen.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.sanaa.presentation.navigation.LocalNavControllerProvider
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
    modifier: Modifier = Modifier
) {
    val viewModel: LoginViewModel = koinViewModel()
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
                    // TODO: navigate to home
                }

                LoginScreenEffects.NavigateToForgotPassword -> {
                    // TODO: navigate to forgot password
                }

                LoginScreenEffects.NavigateToCreateAccount -> {
                    // TODO: navigate to create account
                }

                is LoginScreenEffects.ShowError ->
                    snack = SnackData(effect.message, isError = true)

                is LoginScreenEffects.ShowSuccess ->
                    snack = SnackData(effect.message, isError = false)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginUiState,
    listener: LoginScreenInteractionListener,
    modifier: Modifier = Modifier
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() }
    ) {
        Column(modifier = modifier) {
            LoginTopBar(onBackClick = listener::onBackClicked)

            Spacer(Modifier.height(12.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                LoginHeader()
                Spacer(Modifier.height(40.dp))

                UsernameField(
                    value = state.username,
                    onValueChange = listener::onUsernameChanged
                )
                Spacer(Modifier.height(16.dp))

                PasswordField(
                    value = state.password,
                    onValueChange = listener::onPasswordChanged,
                    isVisible = state.isPasswordVisible,
                    onToggleVisibility = listener::onTogglePasswordVisibility
                )
                Spacer(Modifier.height(32.dp))

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