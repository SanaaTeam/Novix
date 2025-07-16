package com.sanaa.novix.di

import com.sanaa.api.MediaDetailsApi
import com.sanaa.presentation.MediaDetailsApiImpl
import org.koin.dsl.module

val navigationModule = module {
    factory<MediaDetailsApi> { MediaDetailsApiImpl() }
}