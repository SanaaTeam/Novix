package com.sanaa.identity.dataSoruce.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManagerImpl(private val context: Context) : PreferencesManager {
    override val sessionId: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[Companion.sessionId] ?: "" }

    override suspend fun updateSessionId(value: String) {
        context.dataStore.edit { prefs ->
            prefs[Companion.sessionId] = value
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val sessionId = stringPreferencesKey("SESSION_ID")
    }
}