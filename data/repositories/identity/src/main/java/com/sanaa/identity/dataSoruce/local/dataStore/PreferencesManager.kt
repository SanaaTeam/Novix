package com.sanaa.identity.dataSoruce.local.dataStore

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    val sessionId: Flow<String>
    suspend fun updateSessionId(value: String)
}