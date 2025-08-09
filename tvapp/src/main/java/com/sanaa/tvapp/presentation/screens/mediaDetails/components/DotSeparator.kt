package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun DotSeparator() {
    Box(
        modifier = Modifier
            .size(3.dp)
            .background(Theme.colors.body, CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
private fun DotPrev() {
    DotSeparator()
}