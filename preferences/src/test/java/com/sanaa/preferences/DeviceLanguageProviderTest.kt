package com.sanaa.preferences

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

class DeviceLanguageProviderTest {

    @Test
    fun `returns ar when locale is Arabic`() {
        val provider = DeviceLanguageProvider { Locale("ar") }

        val language = provider.getCurrentLanguage()

        assertThat(language).isEqualTo("ar")
    }

    @Test
    fun `returns en when locale is not Arabic`() {
        val provider = DeviceLanguageProvider { Locale("fr") }

        val language = provider.getCurrentLanguage()

        assertThat(language).isEqualTo("en")
    }

    @Test
    fun `returns en when locale is default and not Arabic`() {
        val provider = DeviceLanguageProvider { Locale.ENGLISH }

        val language = provider.getCurrentLanguage()

        assertThat(language).isEqualTo("en")
    }
}