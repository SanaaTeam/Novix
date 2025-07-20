package com.example.preferences

import com.sanaa.preferences.DeviceLanguageProvider
import org.junit.Test
import java.util.Locale
import kotlin.test.assertEquals


class DeviceLanguageProviderTest {

    @Test
    fun `returns ar when locale is Arabic`() {
        val provider = DeviceLanguageProvider { Locale("ar") }
        val language = provider.getCurrentLanguage()
        assertEquals("ar", language)
    }

    @Test
    fun `returns en when locale is not Arabic`() {
        val provider = DeviceLanguageProvider { Locale("fr") }
        val language = provider.getCurrentLanguage()
        assertEquals("en", language)
    }

    @Test
    fun `returns en when locale is default and not Arabic`() {
        val provider = DeviceLanguageProvider { Locale.ENGLISH }
        val language = provider.getCurrentLanguage()
        assertEquals("en", language)
    }
}