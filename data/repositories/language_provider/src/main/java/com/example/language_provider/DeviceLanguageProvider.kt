package com.example.language_provider

import repository.LanguageProvider
import java.util.Locale

class DeviceLanguageProvider(
    private val localeResolver: () -> Locale = { Locale.getDefault() }
) : LanguageProvider {

    override fun getCurrentLanguage(): String {
        return if (localeResolver().language == "ar") "ar" else "en"
    }
}
