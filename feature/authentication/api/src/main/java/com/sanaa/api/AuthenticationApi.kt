package com.sanaa.api

import android.content.Context

interface AuthenticationApi {
    fun launchLogin(context: Context)
    fun launchSignup(context: Context)
    fun launchResetPassword(context: Context)
} 