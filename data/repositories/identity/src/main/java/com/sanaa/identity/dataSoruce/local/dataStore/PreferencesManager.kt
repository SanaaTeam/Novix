package com.sanaa.identity.dataSoruce.local.dataStore

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    val authorizationToken: Flow<String>
    suspend fun updateAuthorizationToken(value: String)
}