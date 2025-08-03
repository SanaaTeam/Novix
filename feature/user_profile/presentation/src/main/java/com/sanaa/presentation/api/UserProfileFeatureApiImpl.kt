package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.presentation.screen.myAccount.MyAccountScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileFeatureApiImpl @Inject constructor() : UserProfileFeatureApi {
    @Composable
    override fun UserProfileScreenApi() {
        MyAccountScreen()
    }
}
