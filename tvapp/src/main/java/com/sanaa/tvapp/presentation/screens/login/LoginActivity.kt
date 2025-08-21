package com.sanaa.tvapp.presentation.screens.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()

        setContent {
            NovixTheme(true) {
                LoginScreenTv(
                    onFinish = {
                        onLoginSuccess()
                    }
                )
            }
        }
    }

    private fun onLoginSuccess() {
        if (callingActivity != null) {
            setResult(RESULT_OK)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}