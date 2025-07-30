package com.sanaa.presentation.api

import android.content.Context
import androidx.compose.runtime.Composable
import com.sanaa.api.AuthenticationApi
import com.sanaa.presentation.navigation.AuthNavHost

class AuthenticationApiImpl : AuthenticationApi {
    @Composable
    override fun AuthenticationScreen(context: Context) {
        AuthNavHost()
    }
}