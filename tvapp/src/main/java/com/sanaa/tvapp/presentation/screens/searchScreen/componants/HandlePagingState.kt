package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.R
import com.sanaa.tvapp.presentation.screens.searchScreen.SearchScreenInteractionListener
import com.sanaa.tvapp.presentation.screens.sharedComponents.TvLoadingIndicator

@Composable
fun <T : Any> HandlePagingState(
    pagingItems: LazyPagingItems<T>,
    searchListener: SearchScreenInteractionListener,
    content: @Composable () -> Unit,
) {
    val refreshState = pagingItems.loadState.refresh
    val isEmpty = pagingItems.itemCount == 0 &&
            refreshState !is LoadState.Loading &&
            refreshState !is LoadState.Error

    when {
        refreshState is LoadState.Loading -> TvLoadingIndicator()
        refreshState is LoadState.Error -> TvErrorStateContent(
            loadStateError = refreshState,
            onRetryClick = { searchListener.onRetryClicked() }
        )
        isEmpty -> TvEmptySearchContent(
            icon = painterResource(id = R.drawable.ic_no_search_result_dark),
            text = stringResource(id = R.string.no_search_result_message)
        )
        else -> content()
    }
}