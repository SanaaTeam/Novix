package com.sanaa.tvapp.presentation.screens.sharedComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TvLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularWavyProgressIndicator(
            modifier = modifier.size(80.dp),
            color = Theme.colors.primary,
            trackColor = Theme.colors.stroke,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun NovixIndicatorPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TvLoadingIndicator()
    }
}