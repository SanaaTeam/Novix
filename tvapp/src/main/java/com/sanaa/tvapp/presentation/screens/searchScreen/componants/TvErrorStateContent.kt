package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import com.sanaa.designsystem.design_system.component.screen_state_content.ErrorStateContent
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import exceptions.NoNetworkException
import com.sanaa.designsystem.R as designSystemResource

@Composable
 fun TvErrorStateContent(loadStateError: LoadState.Error, onRetryClick: () -> Unit) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        if (loadStateError.error is NoNetworkException) {
            NetworkDisconnectionContact(onRetryClick = onRetryClick)
        } else {
            ErrorStateContent(
                onRetryClick = onRetryClick,
                errorTitle = stringResource(designSystemResource.string.error_general_title),
                errorMessage = stringResource(designSystemResource.string.error_general_message)
            )
        }
    }
}