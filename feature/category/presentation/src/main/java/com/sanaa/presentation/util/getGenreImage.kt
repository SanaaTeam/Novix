package com.sanaa.presentation.util

import com.sanaa.feature.category.presentation.R

private val genreImageMap = mapOf(
    // Movies
    28 to R.drawable.movie_action,
    12 to R.drawable.movie_adventure,
    16 to R.drawable.movie_animation,
    35 to R.drawable.movie_comedy,
    80 to R.drawable.movie_crime,
    99 to R.drawable.movie_documenteray,
    18 to R.drawable.movie_drama,
    10751 to R.drawable.movie_family,
    14 to R.drawable.movie_fantasy,
    36 to R.drawable.movie_history,
    27 to R.drawable.movie_horror,
    10402 to R.drawable.movie_music,
    9648 to R.drawable.movie_mystery,
    10749 to R.drawable.movie_romance,
    878 to R.drawable.movie_science_fiction,
    10770 to R.drawable.movie_tv_movie,
    53 to R.drawable.movie_threiller,
    10752 to R.drawable.movie_war,
    37 to R.drawable.movie_western,

    // TV
    10759 to R.drawable.tv_action_adventure,
    10762 to R.drawable.tv_kids,
    10763 to R.drawable.tv_news,
    10764 to R.drawable.tv_reality,
    10765 to R.drawable.tv_sci_fi_fantasy,
    10766 to R.drawable.tv_soap,
    10767 to R.drawable.tv_talk,
    10768 to R.drawable.movie_war
)

fun getGenreImage(genreId: Int): Int {
    return genreImageMap[genreId] ?: R.drawable.movie_tv_movie
}