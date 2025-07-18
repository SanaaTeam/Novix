package com.example.preferences

import android.content.Context
import com.example.preferences.service.GenreLocalizer
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class GenreLocalizerImplTest {

     var context: Context = mockk()

    private lateinit var localizer: GenreLocalizer

    @Before
    fun setup() {
        localizer = GenreLocalizerImpl(context)
    }

    @Test
    fun `returns localized name for DRAMA`() {
        every { context.getString(R.string.genre_drama) } returns "Drama"

        val result = localizer.getLocalizedName("DRAMA")

        assertThat(result).isEqualTo("Drama")
    }

    @Test
    fun `returns localized name for COMEDY`() {
        every { context.getString(R.string.genre_comedy) } returns "Comedy"

        val result = localizer.getLocalizedName("COMEDY")

        assertThat(result).isEqualTo("Comedy")
    }

    @Test
    fun `returns empty string for unknown genre`() {
        val result = localizer.getLocalizedName("UNKNOWN_GENRE")

        assertThat(result).isEmpty()
    }
}