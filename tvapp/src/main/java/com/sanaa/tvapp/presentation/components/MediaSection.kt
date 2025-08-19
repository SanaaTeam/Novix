package com.sanaa.tvapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.tvapp.presentation.screens.home.component.Title

@Composable
fun MediaSection(
    title: String,
    content: LazyListScope.() -> Unit,
) {
    val sidePaddings = 36.dp

    Title(
        modifier = Modifier.padding(horizontal = sidePaddings, vertical = 16.dp),
        title = title
    )

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = sidePaddings, end = sidePaddings, bottom = 12.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}