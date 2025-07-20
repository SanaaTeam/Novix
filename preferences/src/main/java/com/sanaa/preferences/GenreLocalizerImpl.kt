package com.sanaa.preferences

import android.content.Context
import com.sanaa.preferences.service.GenreLocalizer
import preferences.R


class GenreLocalizerImpl(
    private val context: Context
) : GenreLocalizer {
    override fun getLocalizedName(genreName: String): String {
        return when (genreName) {
            "DRAMA" -> context.getString(R.string.genre_drama)
            "COMEDY" -> context.getString(R.string.genre_comedy)
            "ADVENTURE" -> context.getString(R.string.genre_adventure)
            "ACTION" -> context.getString(R.string.genre_action)
            "ROMANCE" -> context.getString(R.string.genre_romance)
            "FANTASY" -> context.getString(R.string.genre_fantasy)
            "SCIENCE_FICTION" -> context.getString(R.string.genre_science_fiction)
            "HORROR" -> context.getString(R.string.genre_horror)
            "CRIME" -> context.getString(R.string.genre_crime)
            "ANIMATION" -> context.getString(R.string.genre_animation)
            "DOCUMENTARY" -> context.getString(R.string.genre_documentary)
            "THRILLER" -> context.getString(R.string.genre_thriller)
            "MUSIC" -> context.getString(R.string.genre_music)
            "MYSTERY" -> context.getString(R.string.genre_mystery)
            "KIDS" -> context.getString(R.string.genre_kids)
            "HISTORY" -> context.getString(R.string.genre_history)
            "FAMILY" -> context.getString(R.string.genre_family)
            "WAR" -> context.getString(R.string.genre_war)
            "TALK" -> context.getString(R.string.genre_talk)
            "SOAP" -> context.getString(R.string.genre_soap)
            "REALITY" -> context.getString(R.string.genre_reality)
            "NEWS" -> context.getString(R.string.genre_news)
            "TV_MOVIE" -> context.getString(R.string.genre_tv_movie)
            "WESTERN" -> context.getString(R.string.genre_western)
            "WAR_AND_POLITICS" -> context.getString(R.string.genre_war_and_politics)
            "SCI_FI_AND_FANTASY" -> context.getString(R.string.genre_sci_fi_and_fantasy)
            "ACTION_AND_ADVENTURE" -> context.getString(R.string.genre_action_and_adventure)
            else -> ""
        }
    }

}