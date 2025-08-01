package com.sanaa.identity.dataSoruce.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesManager {
    override val sessionId: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[SESSION_ID].orEmpty() }

    override val isGuest: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_GUEST] == true }

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