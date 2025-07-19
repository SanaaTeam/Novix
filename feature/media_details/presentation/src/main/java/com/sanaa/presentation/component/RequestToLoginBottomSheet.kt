package com.sanaa.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestToLoginBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onLoginButtonClick: () -> Unit = {},
    text: String = stringResource(R.string.request_login),
    title: String = stringResource(R.string.add_to_list)

) {
    val isDarkTheme = isSystemInDarkTheme()
    val loginImageId = if (isDarkTheme) {
        R.drawable.icon_users_dark
    } else {
        R.drawable.icon_users_light
    }
    BaseBottomSheet(
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AppTopBar(
                    screenTitle = title,
                    rightContent = {
                        TopBarClickableIcon(
                            icon = painterResource(id = R.drawable.icon_cancel),
                            onClick = onDismiss
                        )
                    }
                )
                Image(
                    painter = painterResource
                        (id = loginImageId),
                    contentDescription = "pleas login light",
                    modifier = Modifier.height(100.dp),
                )
                Text(
                    text = text,
                    style = Theme.textStyle.body.small,
                    color = Theme.colors.body,
                    modifier = Modifier.padding(bottom = 24.dp, top = 12.dp)
                )
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = stringResource(R.string.login),
                    onClick = onLoginButtonClick
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun RequestToLoginBottomSheetPreview() {
    val showSheet = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        androidx.compose.material3.Button(onClick = { showSheet.value = true }) {
            Text(text = "Show Bottom Sheet")
        }

        if (showSheet.value) {
            RequestToLoginBottomSheet(
                onDismiss = { showSheet.value = false }
            )
        }
    }
}