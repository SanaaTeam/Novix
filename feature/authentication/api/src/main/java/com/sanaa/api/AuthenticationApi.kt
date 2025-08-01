package com.sanaa.api

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable

interface AuthenticationApi {
    fun getLaunchIntent(context: Context): Intent
} 