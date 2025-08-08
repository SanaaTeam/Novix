package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import entity.Movie
import kotlinx.datetime.LocalDate

@Composable
fun TvShowScreen(modifier: Modifier = Modifier) {
    val movies = listOf(
        Movie(
            id = 1,
            title = "Breaking Bad",
            overview = "A high school chemistry teacher turned methamphetamine producer navigates the criminal underworld.",
            posterImageUrl = "https://example.com/breaking_bad.jpg",
            genres = emptyList(),
            imdbRating = 1f,
            duration = null,
            releaseDate = LocalDate(2023, 10, 1),
            trailerUrl = "TODO()",
            rating = null,
        )
    )
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .background(Theme.colors.surface)
        ) {
//            BackButton({})
            Details(movies[0])
        }
    }
}


@Composable
fun Details(movie: Movie, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(R.drawable.img),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
            )

//        AsyncImage(
//            model = movie.posterImageUrl,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxWidth()
//        )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color.Black,
                                Color.Transparent
                            ),
                            start = Offset.Zero.copy(y = 1000f),
                            end = Offset.Zero.copy(x = 1500f)
                        )
                    )
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 48.dp, vertical = 24.dp)
                .fillMaxWidth(0.5f)
        ) {
            Text(
                text = movie.title,
                style = Theme.textStyle.title.large,
                color = Theme.colors.title
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.overview,
                style = Theme.textStyle.body.small,
                color = Theme.colors.body
            )
        }

    }
}