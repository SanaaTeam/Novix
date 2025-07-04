package com.sanaa.designsystem.design_system.component.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    screenTitle: String = "",
    leftContent: @Composable () -> Unit = {},
    rightContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leftContent()
        Text(
            text = screenTitle,
            style = Theme.textStyle.title.large,
            modifier = Modifier
                .weight(1f),
            color = Theme.colors.title,
        )
        rightContent()
    }
}


@PreviewLightDark
@Composable
private fun AppTopBarPreview() {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 40.dp)
                .background(Theme.colors.surface)
                .fillMaxWidth()
        ) {
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_arrow_back),
                        onClick = {}
                    )

                },
                screenTitle = "Title",
                rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_plus),
                        onClick = {}
                    )
                },
            )
        }
    }
}