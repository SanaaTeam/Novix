package com.sanaa.presentation.navigation

import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchFeatureApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HomeApiEntryPoint {
    fun searchApi(): SearchFeatureApi
    fun detailsApi(): MediaDetailsApi
}