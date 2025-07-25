package com.sanaa.novix.di.login_modules

import com.sanaa.presentation.screen.login.LoginViewModel
import com.sanaa.presentation.screen.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelLoginModule = module {
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::LoginViewModel)
}