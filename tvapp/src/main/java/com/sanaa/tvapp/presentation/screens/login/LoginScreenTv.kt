package com.sanaa.tvapp.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.text_field.PasswordTextField
import com.sanaa.designsystem.design_system.component.text_field.TextField
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.authentication.presentation.R
import com.sanaa.tvapp.presentation.screens.login.components.NovixAnimatedSnackBarHost

@Composable
fun LoginScreenTv(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    var snack by remember { mutableStateOf<SnackData?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginScreenEffects.ShowError -> snack = SnackData(effect.message, true)
                is LoginScreenEffects.ShowSuccess -> snack = SnackData(effect.message, false)
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
        }

        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }
}

@Composable
private fun LoginContentTv(
    state: LoginUiState,
    listener: LoginScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    var usernameFocused by remember { mutableStateOf(false) }
    var passwordFocused by remember { mutableStateOf(false) }
    var buttonFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .width(320.dp),
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { usernameFocused = it.isFocused }
                .focusable(),
            onClick = {},
            border = CardDefaults.border(
                border = Border.None,
                focusedBorder = Border(
                    border = BorderStroke(
                        width = 3.dp,
                        color = Theme.colors.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
            ),
            shape = CardDefaults.shape(RoundedCornerShape(12.dp))
        ) {
            TextField(
                value = state.username,
                onValueChange = listener::onUsernameChanged,
                icon = painterResource(R.drawable.user_name),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { passwordFocused = it.isFocused }
                .focusable(),
            onClick = {},
            border = CardDefaults.border(
                border = Border.None,
                focusedBorder = Border(
                    border = BorderStroke(
                        width = 3.dp,
                        color = Theme.colors.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
            ),
            shape = CardDefaults.shape(RoundedCornerShape(12.dp))
        ) {
            PasswordTextField(
                value = state.password,
                icon = painterResource(R.drawable.lock_key),
                onValueChange = listener::onPasswordChanged,
                isPasswordVisible = state.isPasswordVisible,
                onVisibilityToggle = listener::onTogglePasswordVisibility,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { buttonFocused = it.isFocused }
                .focusable(),
            onClick = listener::onLoginClicked,
            border = CardDefaults.border(
                border = Border.None,
                focusedBorder = Border(
                    border = BorderStroke(
                        width = 3.dp,
                        color = Theme.colors.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
            ),
            shape = CardDefaults.shape(RoundedCornerShape(12.dp))
        ) {
            PrimaryButton(
                text = stringResource(R.string.login),
                onClick = listener::onLoginClicked,
                isEnabled = state.canSubmit,
                isLoading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
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
                    }
                )
            }
        }
    }
}