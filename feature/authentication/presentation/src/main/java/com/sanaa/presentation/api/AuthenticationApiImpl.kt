package com.sanaa.presentation.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.presentation.navigation.AuthNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationApiImpl @Inject constructor() : AuthenticationApi {
    override fun getLaunchIntent(context: Context): Intent {
        return Intent(context, AuthActivity::class.java)
    }
}


@AndroidEntryPoint
class AuthActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        enableEdgeToEdge()

        setContent {
            AuthNavHost()
        }
    }
}