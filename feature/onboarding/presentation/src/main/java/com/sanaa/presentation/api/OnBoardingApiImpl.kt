package com.sanaa.presentation.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import com.sanaa.api.OnboardingApi
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.screen.OnboardingScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class OnBoardingApiImpl @Inject constructor() : OnboardingApi{
    override fun getLaunchIntent(context: Context): Intent {
        return Intent(context,OnBoardingActivity::class.java)
    }
}

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovixTheme(isDarkMode = true) {
                OnboardingScreen(
                    onFinishOnBoarding = {
                        setResult(RESULT_OK)
                        finish()
                    },
                )
            }
        }
    }
}