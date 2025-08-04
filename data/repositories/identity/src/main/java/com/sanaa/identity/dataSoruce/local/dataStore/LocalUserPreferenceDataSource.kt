package com.sanaa.identity.dataSoruce.local.dataStore

import kotlinx.coroutines.flow.Flow

interface LocalUserPreferenceDataSource {
    fun getLanguage(): Flow<String>
    fun getContentRestriction(): Flow<String>
    fun getTheme(): Flow<String>
    suspend fun setTheme(theme: String)
    suspend fun setContentRestriction(contentRestriction: String)
    suspend fun setLanguage(language: String)
}