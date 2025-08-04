package com.sanaa.presentation.navigation

import com.sanaa.api.AuthenticationApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PlayListApiEntryPoint {
    fun authenticationApi(): AuthenticationApi
}