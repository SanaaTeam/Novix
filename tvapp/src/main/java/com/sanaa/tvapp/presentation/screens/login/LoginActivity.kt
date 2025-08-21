package com.sanaa.tvapp.presentation.screens.login

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.MainActivity
import com.sanaa.tvapp.presentation.screens.login.api.LoginApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LoginApiImpl @Inject constructor() : LoginApi {
    override fun launch(context: Context) {
        val intent = LoginActivity.createIntent(context)
        context.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(context as? AppCompatActivity).toBundle()
        )
    }
}

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()

        setContent {
            NovixTheme(true) {
                LoginScreenTv()
            }
        }
    }

    internal companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }
}