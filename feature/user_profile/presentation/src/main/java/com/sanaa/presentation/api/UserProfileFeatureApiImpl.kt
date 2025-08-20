package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.presentation.navigation.ProfileNavHost
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileFeatureApiImpl @Inject constructor() : UserProfileFeatureApi {
    @Composable
    override fun UserProfileScreenApi() {
        val resetKey = remember { System.currentTimeMillis() }
        ProfileNavHost(
            resetKey = resetKey
        )
    }
}