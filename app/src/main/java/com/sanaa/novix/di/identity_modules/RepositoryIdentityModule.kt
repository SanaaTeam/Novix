package com.sanaa.novix.di.identity_modules

import com.sanaa.identity.repository.AuthenticationRepositoryImpl
import com.sanaa.identity.repository.UserPreferenceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.AuthenticationRepository
import repository.UserPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryIdentityModule {

    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindUserPreference(
        userPreferenceImpl: UserPreferenceImpl
    ): UserPreferencesRepository
}