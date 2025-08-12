package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardBorder
import androidx.tv.material3.CardColors
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R

@Composable
fun BackButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.size(36.dp).clip(RoundedCornerShape(40.dp)),
        onClick = {onBackClick()} ,
        colors = CardDefaults.colors(
            containerColor = Theme.colors.iconBackgroundLow,
            contentColor = Theme.colors.surfaceHigh,
            focusedContainerColor = Theme.colors.primary,
            focusedContentColor = Theme.colors.onPrimary
        ),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.icon_back_tringle),
                contentDescription = null,
            )
        }
    }
}


@Preview
@Composable
private fun BackPreview(modifier: Modifier = Modifier) {
    BackButton(onBackClick = {})
}
