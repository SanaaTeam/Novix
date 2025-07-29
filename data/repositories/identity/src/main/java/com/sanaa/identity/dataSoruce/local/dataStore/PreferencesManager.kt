package com.sanaa.identity.dataSoruce.local.dataStore

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    val sessionId: Flow<String>
    val isGuest: Flow<Boolean>
    suspend fun updateSessionId(value: String)
    suspend fun setIsGuest(value: Boolean)
}