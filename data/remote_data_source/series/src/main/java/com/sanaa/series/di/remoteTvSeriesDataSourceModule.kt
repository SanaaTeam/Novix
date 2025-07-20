package com.sanaa.series.di

import com.sanaa.series.RemoteTvSeriesDataSourceImpl
import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteTvSeriesDataSourceModule = module {
    singleOf(::RemoteTvSeriesDataSourceImpl) bind RemoteTvSeriesDataSource::class
}