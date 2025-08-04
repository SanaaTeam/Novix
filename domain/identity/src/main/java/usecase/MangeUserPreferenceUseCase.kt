package usecase

import kotlinx.coroutines.flow.Flow
import repository.ContentRestriction
import repository.Language
import repository.Theme
import repository.UserPreference
import javax.inject.Inject


class MangeUserPreferenceUseCase @Inject constructor(
    private val userPreference: UserPreference
) {
    suspend fun setLanguage(language: Language) {
        userPreference.setLanguage(language)
    }

    fun getLanguage(): Flow<Language> {
        return userPreference.getLanguage()
    }

    fun getTheme(): Flow<Theme> {
        return userPreference.getTheme()
    }

    suspend fun setTheme(theme: Theme) {
        userPreference.setTheme(theme)
    }

    suspend fun setContentRestriction(contentRestriction: ContentRestriction) {
        userPreference.setContentRestriction(contentRestriction)
    }

    fun getContentRestriction(): Flow<ContentRestriction> {
        return userPreference.getContentRestriction()
    }
}