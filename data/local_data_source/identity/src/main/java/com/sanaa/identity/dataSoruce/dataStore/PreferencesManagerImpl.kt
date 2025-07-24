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
    override val authorizationToken: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[AUTHORIZATION_TOKEN].toString() }

    override suspend fun updateAuthorizationToken(value: String) {
        context.dataStore.edit { prefs ->
            prefs[AUTHORIZATION_TOKEN] = value
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val AUTHORIZATION_TOKEN = stringPreferencesKey("AUTHORIZATION_TOKEN")
    }
}