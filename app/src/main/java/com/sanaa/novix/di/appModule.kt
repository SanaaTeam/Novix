package com.sanaa.novix.di

import com.sanaa.search.di.networkModule
import com.sanaa.search.di.remoteDataSource
import org.koin.dsl.module

val appModule = module {
    includes(firebaseModule, loggingModule, networkModule, remoteDataSource)
}