package com.sanaa.presentation.screen.onboardingscreen

import android.content.Context


private val Context.dataStore by preferencesDataStore(name = "onboarding_prefs")
private val ONBOARDING_SHOWN = booleanPreferencesKey("onboarding_shown")

object OnBoardingPreferences {

    suspend fun isShown(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[ONBOARDING_SHOWN] ?: false
    }

    suspend fun markAsShown(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_SHOWN] = true
        }
    }
}