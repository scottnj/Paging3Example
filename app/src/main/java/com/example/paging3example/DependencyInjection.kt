package com.example.paging3example

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainViewModel(get())
    }

    factory {
        MyItemAdapter()
    }

    factory {
        MyDao()
    }
}