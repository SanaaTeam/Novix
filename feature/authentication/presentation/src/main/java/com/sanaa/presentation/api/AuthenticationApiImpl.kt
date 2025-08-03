package com.sanaa.presentation.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sanaa.api.AuthStartRoute
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.StartRoute
import com.sanaa.presentation.navigation.AuthNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationApiImpl @Inject constructor() : AuthenticationApi {
    override fun getLaunchIntent(context: Context,startRoute: AuthStartRoute): Intent {
        return Intent(context, AuthActivity::class.java).apply {
            putExtra(AuthActivity.EXTRA_START_ROUTE,startRoute.name)
        }
    }
}


@AndroidEntryPoint
class AuthActivity : ComponentActivity(){
    companion object{
        const val EXTRA_START_ROUTE = "extra_start_route"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startRoute = intent.getStringExtra(EXTRA_START_ROUTE)?.let { AuthStartRoute.valueOf(it) }

        setContent {
            AuthNavHost(
                startRoute = startRoute?: AuthStartRoute.Welcome,
                onAuthResult = { code:Int->
                    finishWithResultCode(code)
                }
            )
        }
    }



    fun finishWithResultCode(code: Int){
        setResult(code)
        finish()
    }
}