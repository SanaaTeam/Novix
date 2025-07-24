package com.sanaa.novix.di.login_modules

import org.koin.dsl.module

val loginModule = module {
    includes(
        viewModelLoginModule
    )
}