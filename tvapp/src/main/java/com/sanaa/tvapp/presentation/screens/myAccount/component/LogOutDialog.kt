package com.sanaa.tvapp.presentation.screens.myAccount.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.components.DialogBaseComponent

@Composable
fun LogOutDialog(
    onDismissRequest: () -> Unit,
    onLogOutConfirmed: () -> Unit
){

    DialogBaseComponent(
        onDismissRequest = onDismissRequest,
    ) {
        Text(
            text = stringResource(com.sanaa.tvapp.R.string.logout_message),
            color = Theme.colors.title,
            style = Theme.textStyle.label.large,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Button(
            scale = ButtonDefaults.scale(focusedScale = 1.03f),
            shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
            onClick = onLogOutConfirmed,
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.iconBackgroundLow,
                focusedContainerColor = Theme.colors.primary
            ),
        ) {
            Text(
                text = stringResource(com.sanaa.tvapp.R.string.logout),
                style = Theme.textStyle.label.large,
                color = Theme.colors.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}