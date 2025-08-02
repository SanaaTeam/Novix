package com.sanaa.api

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable

interface AuthenticationApi {
    fun getLaunchIntent(context: Context): Intent
    companion object{
        const val RESULT_LOGGED_WITH_SESSION_ID = 1000
        const val RESULT_LOGGED_AS_GUEST = 2000
    }
} 