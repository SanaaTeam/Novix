package com.sanaa.presentation.api

import com.sanaa.api.MediaDetailsApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CategoryApiEntryPoint {
    fun detailsApi(): MediaDetailsApi
}