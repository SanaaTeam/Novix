package com.sanaa.novix.di

import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.presentation.MediaDetailsApiImpl
import com.sanaa.presentation.navigation.SearchFeatureApiImpl
import org.koin.dsl.module

val navigationModule = module {
    factory<SearchFeatureApi> { SearchFeatureApiImpl() }

    factory<MediaDetailsApi> { MediaDetailsApiImpl() }
}