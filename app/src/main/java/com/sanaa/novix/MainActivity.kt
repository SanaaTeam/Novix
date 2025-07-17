package com.sanaa.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.sanaa.presentation.screen.SearchScreen
import com.sanaa.presentation.screen.movie_details.screen.MovieDetailsScreen
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
            MovieDetailsScreen(movieId = 55, navController = rememberNavController())
        }
    }
}