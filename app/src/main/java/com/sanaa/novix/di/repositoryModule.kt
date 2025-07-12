package com.sanaa.novix.di

import com.sanaa.search.repository.SearchRepositoryImpl
import org.koin.dsl.module
import repository.SearchRepository

val repositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get(), get(), get()) }
}
