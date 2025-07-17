package com.sanaa.presentation.component
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun IconWithText(
    iconRes: Int,
    contentDescription: String?,
    text: String,
    textColor:Color = Theme.colors.body,
    tint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            style = Theme.textStyle.label.small,
            color = textColor
        )
    }
}
