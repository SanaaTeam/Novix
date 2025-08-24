package com.sanaa.tvapp.presentation.screens.category.util

import com.sanaa.tvapp.R

private val genreImageMap = mapOf(
    GENRE_MOVIE_ACTION to R.drawable.movie_action,
    GENRE_MOVIE_ADVENTURE to R.drawable.movie_adventure,
    GENRE_MOVIE_ANIMATION to R.drawable.movie_animation,
    GENRE_MOVIE_COMEDY to R.drawable.movie_comedy,
    GENRE_MOVIE_CRIME to R.drawable.movie_crime,
    GENRE_MOVIE_DOCUMENTARY to R.drawable.movie_documenteray,
    GENRE_MOVIE_DRAMA to R.drawable.movie_drama,
    GENRE_MOVIE_FAMILY to R.drawable.movie_family,
    GENRE_MOVIE_FANTASY to R.drawable.movie_fantasy,
    GENRE_MOVIE_HISTORY to R.drawable.movie_history,
    GENRE_MOVIE_HORROR to R.drawable.movie_horror,
    GENRE_MOVIE_MUSIC to R.drawable.movie_music,
    GENRE_MOVIE_MYSTERY to R.drawable.movie_mystery,
    GENRE_MOVIE_ROMANCE to R.drawable.movie_romance,
    GENRE_MOVIE_SCIENCE_FICTION to R.drawable.movie_science_fiction,
    GENRE_MOVIE_TV_MOVIE to R.drawable.movie_tv_movie,
    GENRE_MOVIE_THRILLER to R.drawable.movie_threiller,
    GENRE_MOVIE_WAR to R.drawable.movie_war,
    GENRE_MOVIE_WESTERN to R.drawable.movie_western,

    GENRE_TV_ACTION_ADVENTURE to R.drawable.tv_action_adventure,
    GENRE_TV_KIDS to R.drawable.tv_kids,
    GENRE_TV_NEWS to R.drawable.tv_news,
    GENRE_TV_REALITY to R.drawable.tv_reality,
    GENRE_TV_SCI_FI_FANTASY to R.drawable.tv_sci_fi_fantasy,
    GENRE_TV_SOAP to R.drawable.tv_soap,
    GENRE_TV_TALK to R.drawable.tv_talk,
    GENRE_TV_WAR_POLITICS to R.drawable.movie_war
)

fun getGenreImage(genreId: Int): Int {
    return genreImageMap[genreId] ?: R.drawable.movie_tv_movie
}

const val GENRE_MOVIE_ACTION = 28
const val GENRE_MOVIE_ADVENTURE = 12
const val GENRE_MOVIE_ANIMATION = 16
const val GENRE_MOVIE_COMEDY = 35
const val GENRE_MOVIE_CRIME = 80
const val GENRE_MOVIE_DOCUMENTARY = 99
const val GENRE_MOVIE_DRAMA = 18
const val GENRE_MOVIE_FAMILY = 10751
const val GENRE_MOVIE_FANTASY = 14
const val GENRE_MOVIE_HISTORY = 36
const val GENRE_MOVIE_HORROR = 27
const val GENRE_MOVIE_MUSIC = 10402
const val GENRE_MOVIE_MYSTERY = 9648
const val GENRE_MOVIE_ROMANCE = 10749
const val GENRE_MOVIE_SCIENCE_FICTION = 878
const val GENRE_MOVIE_TV_MOVIE = 10770
const val GENRE_MOVIE_THRILLER = 53
const val GENRE_MOVIE_WAR = 10752
const val GENRE_MOVIE_WESTERN = 37

const val GENRE_TV_ACTION_ADVENTURE = 10759
const val GENRE_TV_KIDS = 10762
const val GENRE_TV_NEWS = 10763
const val GENRE_TV_REALITY = 10764
const val GENRE_TV_SCI_FI_FANTASY = 10765
const val GENRE_TV_SOAP = 10766
const val GENRE_TV_TALK = 10767
const val GENRE_TV_WAR_POLITICS = 10768