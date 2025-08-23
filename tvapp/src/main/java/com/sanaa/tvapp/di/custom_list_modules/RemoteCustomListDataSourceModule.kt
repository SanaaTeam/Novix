package com.sanaa.tvapp.di.custom_list_modules

import com.sanaa.vod.custom_list.RemoteSavedListDataSourceImpl
import com.sanaa.vod.custom_list.SavedListApiService
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteCustomListDataSourceModule {

    @Binds
    abstract fun bindSavedListRemoteDataSource(
        remoteSavedListDataSourceImpl: RemoteSavedListDataSourceImpl,
    ): RemoteSavedListDataSource

    companion object {

        @Provides
        @Singleton
        fun provideSavedListApiService(retrofit: Retrofit): SavedListApiService =
            retrofit.create(SavedListApiService::class.java)
    }
}