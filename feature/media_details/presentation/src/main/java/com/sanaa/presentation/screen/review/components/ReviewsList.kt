package com.sanaa.presentation.screen.review.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.presentation.model.ReviewUiModel

@Composable
fun ReviewsList(pagedReviews: LazyPagingItems<ReviewUiModel>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = pagedReviews.itemCount,
            key = { index ->
                val review = pagedReviews[index]
                "${index}-${review?.id}"
            }
        ) { index ->
            val review = pagedReviews[index] ?: return@items
            ReviewCard(review = review)
        }
    }
}