package com.sanaa.designsystem.design_system.component.snack_bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun NovixSnackBar(
    message: String,
    modifier: Modifier = Modifier,
    isErrorMessage: Boolean = false,
) {
    val animatedIconTintColor by animateColorAsState(
        targetValue = if (isErrorMessage) Theme.colors.statusColors.redAccent else Theme.colors.statusColors.greenAccent
    )
    val iconRes = if (isErrorMessage) R.drawable.icon_alert else R.drawable.icon_two_correct

    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Theme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Theme.colors.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .height(56.dp)
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .width(328.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = animatedIconTintColor
        )
        Text(
            text = message,
            style = Theme.textStyle.body.medium,
            color = Theme.colors.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun PreviewNovixSnackBar() {
    NovixTheme(true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.surfaceHigh)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NovixSnackBar(
                message = "This is a snack bar",
                isErrorMessage = false
            )
        }
    }
}