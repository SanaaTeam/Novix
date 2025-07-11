package com.sanaa.presentation.screen.fake

import com.sanaa.designsystem.R
import com.sanaa.presentation.state.ActorUiModel
import com.sanaa.presentation.state.MovieUiModel
import com.sanaa.presentation.state.TvShowUiModel

object FakeDataProvider {

    val fakeActors = listOf(
        ActorUiModel(
            id = 1,
            name = "Lee Jung-jae",
            imageRes = R.drawable.movie_poster,
            //    playedCharacter = "Peter Parker"
        ),
        ActorUiModel(
            id = 2,
            name = "Zendaya",
            imageRes = R.drawable.movie_poster,
            //    playedCharacter = null
        )
    )

    val fakeMovies = listOf(
        MovieUiModel(1, "The Green Mile", R.drawable.movie_poster, "9.9"),
        MovieUiModel(2, "Prisoners", R.drawable.movie_poster, "9.0"),
        MovieUiModel(3, "Se7en", R.drawable.movie_poster, "8.6"),
        MovieUiModel(3, "Se7en", R.drawable.movie_poster, "8.6"),
        MovieUiModel(3, "Se7en", R.drawable.movie_poster, "8.6"),
        MovieUiModel(3, "Se7en", R.drawable.movie_poster, "8.6"),
        MovieUiModel(4, "Manifest", R.drawable.movie_poster, "8.2")


    )

    val fakeTvShows = listOf(
        TvShowUiModel(
            id = 1,
            title = "Breaking Bad",
            imageRes = R.drawable.movie_poster,
            rating = "9.8"
        )
    )

    val fakeRecentViewedList = listOf(
        R.drawable.movie_poster,
        R.drawable.movie_poster1,
        R.drawable.movie_poster2,
        R.drawable.movie_poster3,
        R.drawable.movie_poster,
        R.drawable.movie_poster1,
        R.drawable.movie_poster2,
        R.drawable.movie_poster3,
        R.drawable.movie_poster,
        R.drawable.movie_poster1,
        R.drawable.movie_poster2,
        R.drawable.movie_poster3,
    )
    val fakeRecentSearchesList = listOf(
        "Shutter island", "Inception", "Tenet",
        "Memento", "Shutter island", "Inception",
        "Tenet", "Memento", "Shutter island", "Inception", "Tenet",
        "Memento", "Shutter island", "Inception",
        "Tenet", "Memento"
    )

}