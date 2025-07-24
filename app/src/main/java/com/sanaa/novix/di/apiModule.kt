package com.sanaa.novix.di

import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.SearchNavigatorApi
import com.sanaa.presentation.navigation.MediaDetailsApiImpl
import com.sanaa.presentation.api.SearchFeatureApiImpl
import com.sanaa.presentation.api.SearchNavigatorApiImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiModule = module {
    factoryOf(::SearchFeatureApiImpl) bind SearchFeatureApi::class
    factoryOf(::MediaDetailsApiImpl) bind MediaDetailsApi::class
    factory<SearchNavigatorApi> { SearchNavigatorApiImpl(get()) }

}