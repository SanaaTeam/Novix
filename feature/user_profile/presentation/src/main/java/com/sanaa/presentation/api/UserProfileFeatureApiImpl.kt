package com.sanaa.presentation.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.designsystem.design_system.component.text.AppText

class UserProfileFeatureApiImpl : UserProfileFeatureApi {
    @Composable
    override fun UserProfileScreenApi() {
        UserProfileScreenPlaceholder()
    }
}

@Composable
private fun UserProfileScreenPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AppText(text = "User Profile Screen", fontSize = 24.sp)
    }
}