package repository

interface LanguageProvider {
    fun getCurrentLanguage(): String
}