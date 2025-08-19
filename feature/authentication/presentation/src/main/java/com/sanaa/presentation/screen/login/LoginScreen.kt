package com.sanaa.presentation.screen.login

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.authentication.presentation.R
import com.sanaa.presentation.navigation.ForgetPasswordRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.SignUpRoute
import com.sanaa.presentation.screen.login.components.LoginActions
import com.sanaa.presentation.screen.login.components.LoginHeader
import com.sanaa.presentation.screen.login.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.screen.login.components.PasswordField
import com.sanaa.presentation.screen.login.components.SignupFooter
import com.sanaa.presentation.screen.login.components.UsernameField
import com.sanaa.designsystem.R as designSystemR

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current

    BackHandler(onBack = viewModel::onBackClicked)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginScreenEffects.NavigateBack -> {
                    val previousAuthScreen = navController.previousBackStackEntry
                    previousAuthScreen?.let {
                        navController.popBackStack()
                    } ?: (navController.context as? Activity)?.finish()
                }

                LoginScreenEffects.ReturnLoggedInResultCode -> {
                    (navController.context as? Activity)?.finish()
                }

                LoginScreenEffects.NavigateToForgotPassword -> {
                    navController.navigate(ForgetPasswordRoute)
                }

                LoginScreenEffects.NavigateToCreateAccount -> {
                    navController.navigate(SignUpRoute)
                }
            }
        }
    }

    LoginContent(
        state = uiState,
        listener = viewModel,
    )
}

@Composable
private fun LoginContent(
    state: LoginUiState,
    listener: LoginScreenInteractionListener,
) {
    NovixScaffold(
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designSystemR.drawable.icon_back),
                        onClick = listener::onBackClicked
                    )
                },
                screenTitle = stringResource(R.string.login),
                modifier = Modifier.statusBarsPadding()
            )
        },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = listener::onSnackBarDismiss
            )
        },
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            LoginHeader()

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(24.dp))

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

@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    NovixTheme {
        LoginContent(
            state = LoginUiState(
                username = "user@example.com",
                password = "password123",
                isPasswordVisible = false,
                isLoading = false,
                canSubmit = true
            ),
            listener = object : LoginScreenInteractionListener {
                override fun onUsernameChanged(newUsername: String) {}
                override fun onPasswordChanged(newPassword: String) {}
                override fun onTogglePasswordVisibility() {}
                override fun onLoginClicked() {}
                override fun onForgotPasswordClicked() {}
                override fun onCreateAccountClicked() {}
                override fun onBackClicked() {}
                override fun onSnackBarDismiss() {}
            },
        )
    }
}