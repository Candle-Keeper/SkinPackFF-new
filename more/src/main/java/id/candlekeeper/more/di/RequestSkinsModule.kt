package id.candlekeeper.more.di

import id.candlekeeper.more.MoreViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moreModule = module {
    viewModel {
        MoreViewModel(get())
    }
}