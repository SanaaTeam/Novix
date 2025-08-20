package com.sanaa.presentation.screen.myAccount.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun VerticalList(items: List<AccountOptionItem>) {
    Column(
        modifier = Modifier
    ) {
        items.forEachIndexed { index, item ->
            AccountOption(
                painter = item.painter,
                title = item.title,
                onClick = item.onClick,
                description = item.description
            )

            if (index != items.lastIndex)
                Divider()
        }
    }
}

@Composable
fun Divider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(Theme.colors.stroke)
    )
}