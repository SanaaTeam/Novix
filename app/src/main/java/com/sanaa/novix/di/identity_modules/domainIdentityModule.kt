package com.sanaa.novix.di.identity_modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import usecase.LoginUseCase

val domainIdentityModule = module {
    singleOf(::LoginUseCase)
}