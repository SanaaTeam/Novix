package com.sanaa.presentation.api

import android.content.Context
import androidx.compose.runtime.Composable
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.presentation.navigation.AuthNavHost
import javax.inject.Inject

class AuthenticationApiImpl @Inject constructor(
    private val homeFeatureApi: HomeFeatureApi,
) : AuthenticationApi {
    @Composable
    override fun AuthenticationScreen(context: Context) {
        AuthNavHost(homeFeatureApi)
    }
}