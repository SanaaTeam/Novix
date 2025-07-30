package com.sanaa.novix.di.identity_modules

import androidx.datastore.core.DataStore
import com.sanaa.identity.dataSoruce.dataStore.LocalUserDataSourceImpl
import com.sanaa.identity.dataSoruce.dataStore.PreferencesManagerImpl
import com.sanaa.identity.dataSoruce.dataStore.provideUserDataStore
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.proto.User
import com.sanaa.novix.resourceProvider.StringProviderImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import service.StringProvider

val servicesModule = module {
    singleOf(::StringProviderImpl) bind StringProvider::class
    singleOf(::PreferencesManagerImpl) bind PreferencesManager::class

    single<DataStore<User>> {
        provideUserDataStore(get())
    }
    singleOf(::LocalUserDataSourceImpl) bind LocalUserDataSource::class
}