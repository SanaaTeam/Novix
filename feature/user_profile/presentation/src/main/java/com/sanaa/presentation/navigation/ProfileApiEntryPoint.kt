package com.sanaa.presentation.navigation

import com.sanaa.api.AuthenticationApi
import com.sanaa.api.MediaDetailsApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProfileApiEntryPoint {
    fun detailsApi(): MediaDetailsApi
    fun authenticationApi(): AuthenticationApi
}