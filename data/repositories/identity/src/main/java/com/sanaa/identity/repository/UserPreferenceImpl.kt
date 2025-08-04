package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserPreferenceDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import repository.ContentRestriction
import repository.Language
import repository.Theme
import repository.UserPreferencesRepository
import javax.inject.Inject

class UserPreferenceImpl
@Inject constructor(
    private val localDataSource: LocalUserPreferenceDataSource
) : UserPreferencesRepository {
    override fun getLanguage(): Flow<Language> {
        return localDataSource.getLanguage().map { code ->
            Language.entries.first { it.code == code }
        }
    }

    override fun getContentRestriction(): Flow<ContentRestriction> {
        return localDataSource.getContentRestriction().map {
            ContentRestriction.valueOf(it)
        }
    }

    override fun getTheme(): Flow<Theme> {
        return localDataSource.getTheme().map {
            Theme.valueOf(it)
        }
    }

    override suspend fun setTheme(theme: Theme) {
        localDataSource.setTheme(theme.name)
    }

    override suspend fun setContentRestriction(contentRestriction: ContentRestriction) {
        localDataSource.setContentRestriction(contentRestriction.name)
    }

    override suspend fun setLanguage(language: Language) {
        localDataSource.setLanguage(language.code)
    }
}