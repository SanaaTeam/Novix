package com.sanaa.presentation.screen.movie_categories

import androidx.compose.material3.BottomAppBarState
import entity.Genre

data class MovieCategoriesScreenUiState(
    val title: String = "",
    val movies: List<MovieCardUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
    )

data class MovieCardUiModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val rating: Float = 0.0f,
)

val GenreDisplayNameMap = mapOf(
    Genre.ACTION to "Action",
    Genre.ADVENTURE to "Adventure",
    Genre.COMEDY to "Comedy",
    Genre.DRAMA to "Drama",
    Genre.HORROR to "Horror",
    Genre.SCIENCE_FICTION to "Science Fiction",
    Genre.FANTASY to "Fantasy",
    Genre.ROMANCE to "Romance",
    Genre.THRILLER to "Thriller",
    Genre.DOCUMENTARY to "Documentary",
    Genre.ANIMATION to "Animation",
    Genre.CRIME to "Crime",
    Genre.FAMILY to "Family",
    Genre.HISTORY to "History",
    Genre.KIDS to "Kids",
    Genre.MYSTERY to "Mystery",
    Genre.MUSIC to "Music",
    Genre.NEWS to "News",
    Genre.REALITY to "Reality",
    Genre.SOAP to "Soap",
    Genre.TALK to "Talk Show",
    Genre.WAR to "War",
    Genre.WAR_AND_POLITICS to "War and Politics",
    Genre.WESTERN to "Western",
    Genre.TV_MOVIE to "TV Movie",
    Genre.ACTION_AND_ADVENTURE to "Action & Adventure",
    Genre.SCI_FI_AND_FANTASY to "Sci-Fi & Fantasy"
)
