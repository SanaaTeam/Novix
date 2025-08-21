package com.sanaa.tvapp.presentation.screens.myAccount

import com.sanaa.tvapp.presentation.screens.login.api.LoginApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MyAccountApiEntryPoint {
    fun loginApi(): LoginApi
}