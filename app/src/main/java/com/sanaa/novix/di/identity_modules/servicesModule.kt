package com.sanaa.novix.di.identity_modules

import com.sanaa.identity.dataSoruce.dataStore.PreferencesManagerImpl
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
//import com.sanaa.identity.service.AuthenticationService
//import com.sanaa.identity.service.AuthenticationServiceImpl
import com.sanaa.novix.resourceProvider.StringProviderImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import service.StringProvider

val servicesModule = module {
    singleOf(::StringProviderImpl) bind StringProvider::class
//    singleOf(::AuthenticationServiceImpl) bind AuthenticationService::class
    singleOf(::PreferencesManagerImpl) bind PreferencesManager::class
}