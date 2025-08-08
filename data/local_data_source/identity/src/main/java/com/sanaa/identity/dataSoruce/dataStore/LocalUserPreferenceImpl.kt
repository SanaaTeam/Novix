package com.sanaa.identity.dataSoruce.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserPreferenceDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

class LocalUserPreferenceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalUserPreferenceDataSource {

    override fun getLanguage(): Flow<String> {
        return context.dataStore.data.map {
            it[LANGUAGE] ?: getAppCurrentLanguage(context)
        }
    }

    override fun getContentRestriction(): Flow<String> {
        return context.dataStore.data.map {
            it[CONTENT_RESTRICTION] ?: DEFAULT_CONTENT_RESTRICTION
        }
    }

    override fun getTheme(): Flow<String> {
        return context.dataStore.data.map {
            it[THEME] ?: DEFAULT_THEME
        }
    }

    override suspend fun setTheme(theme: String) {
        context.dataStore.edit {
            it[THEME] = theme
        }
    }

    override suspend fun setContentRestriction(contentRestriction: String) {
        context.dataStore.edit {
            it[CONTENT_RESTRICTION] = contentRestriction
        }
    }

    override suspend fun setLanguage(language: String) {
        context.dataStore.edit {
            it[LANGUAGE] = language
        }
    }

    private companion object {
        val LANGUAGE = stringPreferencesKey("language")
        val CONTENT_RESTRICTION = stringPreferencesKey("content_restriction")
        val THEME = stringPreferencesKey("theme")

        fun getAppCurrentLanguage(context: Context): String {
            val config = context.resources.configuration
            return config.locales[0].language
        }

        const val DEFAULT_CONTENT_RESTRICTION = "RESTRICTED"
        const val DEFAULT_THEME = "DARK"
    }
}