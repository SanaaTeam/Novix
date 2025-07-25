package com.sanaa.novix.di.identity_modules

import com.sanaa.identity.repository.AuthenticationRepositoryImpl
import com.sanaa.identity.service.AuthenticationService
import com.sanaa.identity.service.AuthenticationServiceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import repository.AuthenticationRepository

val repositoryIdentityModule = module {
    singleOf(::AuthenticationRepositoryImpl) bind AuthenticationRepository::class
    singleOf(::AuthenticationServiceImpl) bind AuthenticationService::class
}