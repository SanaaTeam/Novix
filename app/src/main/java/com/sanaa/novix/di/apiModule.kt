package com.sanaa.novix.di

import com.sanaa.api.AuthenticationApi
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.presentation.api.AuthenticationApiImpl
import com.sanaa.presentation.api.SearchFeatureApiImpl
import com.sanaa.presentation.navigation.MediaDetailsApiImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiModule = module {
    factoryOf(::AuthenticationApiImpl) bind AuthenticationApi::class
    factoryOf(::SearchFeatureApiImpl) bind SearchFeatureApi::class
    factoryOf(::MediaDetailsApiImpl) bind MediaDetailsApi::class
}