package com.example.preferences

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.sanaa.preferences.GenreLocalizerImpl
import com.sanaa.preferences.R
import com.sanaa.preferences.service.GenreLocalizer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class GenreLocalizerImplTest {

    @MockK
    lateinit var context: Context

    private lateinit var localizer: GenreLocalizer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        localizer = GenreLocalizerImpl(context)

        // Map of genre name (as string) to expected Arabic translation
        val arabicTranslations = mapOf(
            "DRAMA" to "دراما",
            "COMEDY" to "كوميديا",
            "ADVENTURE" to "مغامرة",
            "ACTION" to "أكشن",
            "ROMANCE" to "رومانسي",
            "FANTASY" to "فانتازيا",
            "SCIENCE_FICTION" to "خيال علمي",
            "HORROR" to "رعب",
            "CRIME" to "جريمة",
            "ANIMATION" to "رسوم متحركة",
            "DOCUMENTARY" to "وثائقي",
            "THRILLER" to "إثارة",
            "MUSIC" to "موسيقى",
            "MYSTERY" to "غموض",
            "KIDS" to "أطفال",
            "HISTORY" to "تاريخ",
            "FAMILY" to "عائلي",
            "WAR" to "حرب",
            "TALK" to "حوارات",
            "SOAP" to "دراما طويلة",
            "REALITY" to "واقعي",
            "NEWS" to "أخبار",
            "TV_MOVIE" to "أفلام تلفزيونية",
            "WESTERN" to "غربي",
            "WAR_AND_POLITICS" to "الحروب والسياسة",
            "SCI_FI_AND_FANTASY" to "خيال علمي وفانتازيا",
            "ACTION_AND_ADVENTURE" to "أكشن ومغامرة"
        )

        // Mock context.getString(R.string.genre_xxx) for each genre
        arabicTranslations.forEach { (genreKey, arabicValue) ->
            val resId = when (genreKey) {
                "DRAMA" -> R.string.genre_drama
                "COMEDY" -> R.string.genre_comedy
                "ADVENTURE" -> R.string.genre_adventure
                "ACTION" -> R.string.genre_action
                "ROMANCE" -> R.string.genre_romance
                "FANTASY" -> R.string.genre_fantasy
                "SCIENCE_FICTION" -> R.string.genre_science_fiction
                "HORROR" -> R.string.genre_horror
                "CRIME" -> R.string.genre_crime
                "ANIMATION" -> R.string.genre_animation
                "DOCUMENTARY" -> R.string.genre_documentary
                "THRILLER" -> R.string.genre_thriller
                "MUSIC" -> R.string.genre_music
                "MYSTERY" -> R.string.genre_mystery
                "KIDS" -> R.string.genre_kids
                "HISTORY" -> R.string.genre_history
                "FAMILY" -> R.string.genre_family
                "WAR" -> R.string.genre_war
                "TALK" -> R.string.genre_talk
                "SOAP" -> R.string.genre_soap
                "REALITY" -> R.string.genre_reality
                "NEWS" -> R.string.genre_news
                "TV_MOVIE" -> R.string.genre_tv_movie
                "WESTERN" -> R.string.genre_western
                "WAR_AND_POLITICS" -> R.string.genre_war_and_politics
                "SCI_FI_AND_FANTASY" -> R.string.genre_sci_fi_and_fantasy
                "ACTION_AND_ADVENTURE" -> R.string.genre_action_and_adventure
                else -> throw IllegalArgumentException("Unknown genre: $genreKey")
            }
            every { context.getString(resId) } returns arabicValue
        }
    }

    @Test
    fun `should return correct Arabic translation for all genres`() {
        val genreList = listOf(
            "DRAMA", "COMEDY", "ADVENTURE", "ACTION", "ROMANCE", "FANTASY",
            "SCIENCE_FICTION", "HORROR", "CRIME", "ANIMATION", "DOCUMENTARY",
            "THRILLER", "MUSIC", "MYSTERY", "KIDS", "HISTORY", "FAMILY",
            "WAR", "TALK", "SOAP", "REALITY", "NEWS", "TV_MOVIE", "WESTERN",
            "WAR_AND_POLITICS", "SCI_FI_AND_FANTASY", "ACTION_AND_ADVENTURE"
        )

        val expectedArabic = listOf(
            "دراما", "كوميديا", "مغامرة", "أكشن", "رومانسي", "فانتازيا",
            "خيال علمي", "رعب", "جريمة", "رسوم متحركة", "وثائقي",
            "إثارة", "موسيقى", "غموض", "أطفال", "تاريخ", "عائلي",
            "حرب", "حوارات", "دراما طويلة", "واقعي", "أخبار", "أفلام تلفزيونية", "غربي",
            "الحروب والسياسة", "خيال علمي وفانتازيا", "أكشن ومغامرة"
        )

        val localized = genreList.map { localizer.getLocalizedName(it) }

        assertThat(localized).isEqualTo(expectedArabic)
    }
}