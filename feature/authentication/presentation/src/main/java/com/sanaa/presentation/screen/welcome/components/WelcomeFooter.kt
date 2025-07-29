package com.sanaa.presentation.screen.welcome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.NovixOutlinedButton
import com.sanaa.designsystem.design_system.component.button.NovixPrimaryButton
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.authentication.presentation.R

@Composable
fun WelcomeFooter(
    onLoginClicked: () -> Unit,
    onContinueClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WelcomeInfo()
        Spacer(Modifier.height(32.dp))
        WelcomeActions(
            onLoginClicked = onLoginClicked,
            onContinueClicked = onContinueClicked
        )
    }
}

@Composable
private fun WelcomeInfo() {
    Text(
        text = stringResource(R.string.welcome_to_novix),
        style = Theme.textStyle.title.large,
        color = Theme.colors.title
    )
    Spacer(Modifier.height(12.dp))
    Text(
        text = stringResource(R.string.onboarding_caption),
        style = Theme.textStyle.body.small,
        color = Theme.colors.body,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun WelcomeActions(
    onLoginClicked: () -> Unit,
    onContinueClicked: () -> Unit
) {
    NovixPrimaryButton(
        text = stringResource(R.string.login),
        onClick = onLoginClicked,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(12.dp))
    NovixOutlinedButton(
        text = stringResource(R.string.continue_as_guest),
        onClick = onContinueClicked,
        modifier = Modifier.fillMaxWidth()
    )
}