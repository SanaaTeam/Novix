package com.sanaa.presentation.screen.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestToLoginBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    text: String = "Please login to save to list"

) {
    BaseBottomSheet(
        onDismiss = onDismiss,
        content = {
            Column(modifier = Modifier) {
                AppTopBar(
                    screenTitle = "Title",
                    rightContent = {
                        TopBarClickableIcon(
                            icon = painterResource(id = R.drawable.icon_plus), onClick = {})
                    }
                )
                Column {
                    Image(
                        painter = painterResource(id = com.sanaa.presentation.R.drawable.pleas_login_light),
                        contentDescription = "pleas login light"
                    )
                    Text(
                        text = text,
                        style = Theme.textStyle.title.large,
                        modifier = Modifier.weight(1f),
                        color = Theme.colors.title,
                    )
                }
            }

        }
    )
}


@Preview
@Composable
private fun RequestToLoginBottomSheetPreview() {
    RequestToLoginBottomSheet()
}