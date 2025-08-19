package com.sanaa.presentation.util

import com.sanaa.feature.category.presentation.R

const val IMAGE_RAW_BASE_URL = "https://raw.githubusercontent.com/SanaaTeam/Novix/refs/heads/enhancement/decrease-app-size/feature/category/presentation/src/main/assets/genre-images"
// Movie genre IDs
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

// TV genre IDs
const val GENRE_TV_ACTION_ADVENTURE = 10759
const val GENRE_TV_KIDS = 10762
const val GENRE_TV_NEWS = 10763
const val GENRE_TV_REALITY = 10764
const val GENRE_TV_SCI_FI_FANTASY = 10765
const val GENRE_TV_SOAP = 10766
const val GENRE_TV_TALK = 10767
const val GENRE_TV_WAR_POLITICS = 10768



private val genreImageMap = mapOf(
    // Movies
    GENRE_MOVIE_ACTION to "movie_action",
    GENRE_MOVIE_ADVENTURE to "movie_adventure",
    GENRE_MOVIE_ANIMATION to "movie_animation",
    GENRE_MOVIE_COMEDY to "movie_comedy",
    GENRE_MOVIE_CRIME to "movie_crime",
    GENRE_MOVIE_DOCUMENTARY to "movie_documenteray",
    GENRE_MOVIE_DRAMA to "movie_drama",
    GENRE_MOVIE_FAMILY to  "movie_family",
    GENRE_MOVIE_FANTASY to  "movie_fantasy",
    GENRE_MOVIE_HISTORY to  "movie_history",
    GENRE_MOVIE_HORROR to  "movie_horror",
    GENRE_MOVIE_MUSIC to "movie_music",
    GENRE_MOVIE_MYSTERY to "movie_mystery",
    GENRE_MOVIE_ROMANCE to "movie_romance",
    GENRE_MOVIE_SCIENCE_FICTION to "movie_science_fiction",
    GENRE_MOVIE_TV_MOVIE to "movie_tv_movie",
    GENRE_MOVIE_THRILLER to "movie_threiller",
    GENRE_MOVIE_WAR to "movie_war",
    GENRE_MOVIE_WESTERN to "movie_western",

    // TV
    GENRE_TV_ACTION_ADVENTURE to "tv_action_adventure",
    GENRE_TV_KIDS to "tv_kids",
    GENRE_TV_NEWS to "tv_news",
    GENRE_TV_REALITY to "tv_reality",
    GENRE_TV_SCI_FI_FANTASY to "tv_sci_fi_fantasy",
    GENRE_TV_SOAP to "tv_soap",
    GENRE_TV_TALK to "tv_talk",
    GENRE_TV_WAR_POLITICS to "movie_war"
)

fun getGenreImage(genreId: Int): String {
    return "$IMAGE_RAW_BASE_URL/${genreImageMap[genreId]}.webp"
}
