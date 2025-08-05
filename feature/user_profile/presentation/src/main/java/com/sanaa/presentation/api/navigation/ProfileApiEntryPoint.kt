package com.sanaa.presentation.api.navigation

import com.sanaa.api.MediaDetailsApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProfileApiEntryPoint {
    fun detailsApi(): MediaDetailsApi
}