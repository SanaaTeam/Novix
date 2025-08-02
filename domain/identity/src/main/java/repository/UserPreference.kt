package repository

import kotlinx.coroutines.flow.Flow

interface UserPreference {
    fun getLanguage(): Flow<Language>
    fun getContentRestriction(): Flow<ContentRestriction>
    fun getTheme(): Flow<Theme>
    suspend fun setTheme(theme: Theme)
    suspend fun setContentRestriction(contentRestriction: ContentRestriction)
    suspend fun setLanguage(language: Language)
}

enum class Language {
    ENGLISH, ARABIC, UNSPECIFIED
}

enum class ContentRestriction {
    RESTRICTED,
    UNRESTRICTED,
    MODERATE_RESTRICTION
}

enum class Theme {
    LIGHT,
    DARK
}