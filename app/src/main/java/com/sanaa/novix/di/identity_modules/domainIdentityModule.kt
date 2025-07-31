package com.sanaa.novix.di.identity_modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.LoginUseCase

val domainIdentityModule = module {
    singleOf(::LoginUseCase)
    singleOf(::CheckIfUserIsLoggedInUseCase)
    singleOf(::GetLoggedInUserUseCase)
}