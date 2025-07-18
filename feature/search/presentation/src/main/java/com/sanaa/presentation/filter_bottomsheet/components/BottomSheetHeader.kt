package com.sanaa.presentation.filter_bottomsheet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun BottomSheetHeader(onCancelClicked: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.filter),
            style = Theme.textStyle.title.large,
            color = Theme.colors.title
        )
        Box(
            modifier = Modifier
                .background(
                    color = Theme.colors.iconBackgroundLow,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(onClick = onCancelClicked)
                .border(
                    width = 1.dp,
                    color = Theme.colors.stroke,
                    shape = RoundedCornerShape(8.dp)

                )
                .padding(8.dp)

        ) {
            Icon(
                painter = painterResource(R.drawable.cancel),
                contentDescription = stringResource(R.string.cancel),
                modifier = Modifier.size(16.dp),
                tint = Theme.colors.title
            )
        }
    }
}

@PreviewLightDark
@Preview
@Composable
private fun PreviewBottomSheetHeader() {
    NovixTheme(isSystemInDarkTheme()) {
        BottomSheetHeader()
    }
}