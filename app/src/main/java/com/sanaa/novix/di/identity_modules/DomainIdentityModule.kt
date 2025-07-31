package com.sanaa.novix.di.identity_modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.AuthenticationRepository
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.LoginUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainIdentityModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(
        authenticationRepository: AuthenticationRepository
    ): LoginUseCase {
        return LoginUseCase(authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideCheckIfUserIsLoggedInUseCase(
        authenticationRepository: AuthenticationRepository
    ): CheckIfUserIsLoggedInUseCase {
        return CheckIfUserIsLoggedInUseCase(authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideGetLoggedInUserUseCase(
        authenticationRepository: AuthenticationRepository
    ): GetLoggedInUserUseCase {
        return GetLoggedInUserUseCase(authenticationRepository)
    }
}