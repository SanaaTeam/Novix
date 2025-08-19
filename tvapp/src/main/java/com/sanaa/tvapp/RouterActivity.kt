package com.sanaa.tvapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.sanaa.tvapp.presentation.screens.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import usecase.CheckIfUserIsLoggedInUseCase
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class RouterActivity : ComponentActivity() {

    @Inject
    lateinit var checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isLoggedIn = checkIfUserIsLoggedInUseCase.isLoggedIn().first()

            val targetActivity = if (isLoggedIn) {
                MainActivity::class.java
            } else {
                LoginActivity::class.java
            }

            val intent = Intent(this@RouterActivity, targetActivity).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)

            finish()
        }
    }
}