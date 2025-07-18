package com.sanaa.series.di

import com.sanaa.series.RemoteTvSeriesDataSourceImpl
import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import org.koin.dsl.module

val remoteTvSeriesDataSourceModule = module {
    single<RemoteTvSeriesDataSource> {
        RemoteTvSeriesDataSourceImpl(
            get(), get()
        )
    }

}