package com.sanaa.identity.dataSoruce.local.dataStore

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    val sessionId: Flow<String>
    val isGuest: Flow<Boolean>
    val accountId: Flow<Long>
    val isFirstLaunch:Flow<Boolean>
    suspend fun disableFirstLaunch()
    suspend fun updateSessionId(value: String)
    suspend fun setIsGuest(value: Boolean)
    suspend fun setAccountId(value: Long)
    suspend fun clearSession()
}