package com.sanaa.novix.di

import com.sanaa.presentation.filter_bottomsheet.FilterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel { FilterViewModel() }
}