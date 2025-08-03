package com.sanaa.api

import android.content.Context
import android.content.Intent

interface AuthenticationApi {
    fun getLaunchIntent(context: Context,startRoute: AuthStartRoute = AuthStartRoute.Login): Intent
    companion object{
        const val RESULT_LOGGED_WITH_SESSION_ID = 1000
        const val RESULT_LOGGED_AS_GUEST = 2000
    }
}

enum class AuthStartRoute {
    Welcome, Login, SignUp, ForgetPassword
}