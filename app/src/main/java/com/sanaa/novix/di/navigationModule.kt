package com.sanaa.novix.di

import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.presentation.navigation.SearchFeatureApiImpl
import com.sanaa.presentation.ui.MediaDetailsApiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule = module {
    singleOf(::SearchFeatureApiImpl)bind SearchFeatureApi::class
    singleOf(::MediaDetailsApiImpl)bind MediaDetailsApi::class
    singleOf(::SearchFeatureApiImpl)
    singleOf(::MediaDetailsApiImpl)

}