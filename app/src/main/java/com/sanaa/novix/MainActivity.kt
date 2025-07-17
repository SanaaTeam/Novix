package com.sanaa.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.analytics.FirebaseAnalytics
import com.sanaa.presentation.navigation.DetailsNavHost
import com.sanaa.presentation.navigation.StartRoute
import com.sanaa.presentation.screen.ActorScreen
import org.koin.android.ext.android.getKoin
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        analytics = getKoin().get()

        Timber.d("MainActivity created")

        setContent {
            DetailsNavHost(
                startRoute = StartRoute.ACTOR,
                id = 5
            )
        }
    }
}