package com.sanaa.presentation.util

import com.sanaa.api.HomeFeatureApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HomeApiEntryPoint {
    fun homeApi(): HomeFeatureApi
}