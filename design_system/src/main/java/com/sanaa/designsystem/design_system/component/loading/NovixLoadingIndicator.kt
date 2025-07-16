package com.sanaa.designsystem.design_system.component.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NovixLoadingIndicator() {
    CircularWavyProgressIndicator(
        modifier = Modifier.size(64.dp),
        color = Theme.colors.primary,
        trackColor = Theme.colors.stroke,
    )
}

@Preview(showBackground = true)
@Composable
fun WavyProgressIndicatorPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NovixLoadingIndicator()
    }
}