package com.sanaa.identity.dataSoruce.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManagerImpl(private val context: Context) : PreferencesManager {
    override val sessionId: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[SESSION_ID] ?: "" }

    override val isGuest: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_GUEST] ?: false }

    override suspend fun updateSessionId(value: String) {
        context.dataStore.edit { prefs ->
            prefs[SESSION_ID] = value
        }
    }

    override suspend fun setIsGuest(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_GUEST] = value
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val SESSION_ID = stringPreferencesKey("SESSION_ID")
        val IS_GUEST = booleanPreferencesKey("IS_GUEST")
    }
}