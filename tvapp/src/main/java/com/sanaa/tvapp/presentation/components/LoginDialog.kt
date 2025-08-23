package com.sanaa.tvapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun LoginDialog(
    onDismissRequest: () -> Unit = {},
    onLoginClicked: () -> Unit = {},
) {
    DialogBaseComponent(
        onDismissRequest = onDismissRequest
    ) {
        Image(
            painter = painterResource(id = com.sanaa.tvapp.R.drawable.users_placeholder),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = stringResource(com.sanaa.tvapp.R.string.please_login_to_rate_your_favorite_items),
            color = Theme.colors.title,
            style = Theme.textStyle.label.large,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Button(
            scale = ButtonDefaults.scale(focusedScale = 1.03f),
            shape = ButtonDefaults.shape(RoundedCornerShape(12.dp)),
            onClick = onLoginClicked,
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.iconBackgroundLow,
                focusedContainerColor = Theme.colors.primary
            ),
        ) {
            Text(
                text = stringResource(com.sanaa.tvapp.R.string.login),
                style = Theme.textStyle.label.large,
                color = Theme.colors.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true, device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
private fun LoginDialogPreview() {
    NovixTheme(isDarkMode = true) {
        LoginDialog()
    }
}