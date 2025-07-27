package com.sanaa.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sanaa.api.HomeFeatureApi
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var homeFeatureApi: HomeFeatureApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Timber.d("MainActivity created")

        setContent {
            homeFeatureApi.HomeScreenApi()
        }
    }
}
