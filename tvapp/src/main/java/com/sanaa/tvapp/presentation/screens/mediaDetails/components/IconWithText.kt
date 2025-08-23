package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun IconWithText(
    iconRes: Int,
    contentDescription: String? = "",
    text: String,
    textColor: Color = Theme.colors.body,
    tint: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(tint),
            modifier = Modifier.size(12.dp)
        )
        AppText(
            text = text,
            style = Theme.textStyle.label.small,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IconWithTextPrev() {
    IconWithText(
        iconRes = com.sanaa.designsystem.R.drawable.cancel,
        contentDescription = "test",
        text = "test",
        tint = Theme.colors.hint
    )
}