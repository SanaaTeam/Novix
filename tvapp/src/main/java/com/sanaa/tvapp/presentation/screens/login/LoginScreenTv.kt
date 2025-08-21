package com.sanaa.tvapp.presentation.screens.login

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.button.common.AnimatedLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.text_field.PasswordTextField
import com.sanaa.designsystem.design_system.component.text_field.TextField
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.login.components.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.state.SnackData


@Composable
fun LoginScreenTv(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    var snack by remember { mutableStateOf<SnackData?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginScreenEffects.ShowError -> snack = SnackData(effect.message, true)
                is LoginScreenEffects.ShowSuccess -> snack = SnackData(effect.message, false)
                LoginScreenEffects.ReturnGuestResultCode -> (context as? Activity)?.finish()
            }
        }
    }

    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoginContentTv(
                state = uiState,
                listener = viewModel,
            )

            NovixAnimatedSnackBarHost(
                modifier = Modifier.align(Alignment.TopCenter),
                data = snack,
                onDismiss = { snack = null }
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun LoginContentTv(
    state: LoginUiState,
    listener: LoginScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(320.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = stringResource(R.string.app_icon),
            modifier = Modifier
                .padding(bottom = 38.dp)
                .size(88.dp)
        )

        TextField(
            value = state.username,
            onValueChange = listener::onUsernameChanged,
            icon = painterResource(R.drawable.user_name),
            modifier = Modifier.fillMaxWidth()
        )


        PasswordTextField(
            value = state.password,
            icon = painterResource(R.drawable.lock_key),
            onValueChange = listener::onPasswordChanged,
            isPasswordVisible = state.isPasswordVisible,
            onVisibilityToggle = listener::onTogglePasswordVisibility,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.canSubmit,
            scale = ButtonDefaults.scale(focusedScale = 1.03f),
            shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
            onClick = { listener.onLoginClicked() },
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.iconBackgroundLow,
                focusedContainerColor = Theme.colors.primary
            ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = Theme.textStyle.label.large,
                    color = Theme.colors.onPrimary,
                    textAlign = TextAlign.Center
                )

                AnimatedVisibility(state.isLoading) {
                    AnimatedLoadingIndicator(
                        modifier = Modifier.padding(start = 8.dp),
                        iconTint = Theme.colors.onPrimary,
                        size = 20.dp,
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            scale = ButtonDefaults.scale(focusedScale = 1.03f),
            shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
            onClick = { listener.onContinueClicked() },
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.surface,
                focusedContainerColor = Theme.colors.primaryVariant
            ),
            border = ButtonDefaults.border(
                border = Border(BorderStroke(width = 1.dp, color = Theme.colors.stroke))
            ),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.continue_as_guest),
                style = Theme.textStyle.label.large,
                color = Theme.colors.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(widthDp = 960, heightDp = 540)
@Composable
fun PreviewLoginScreenTv() {
    NovixTheme {
        NovixScaffold(backgroundShapes = { BackgroundShapes() }) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoginContentTv(
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
                        override fun onContinueClicked() {}
                    }
                )
            }
        }
    }
}