package id.candlekeeper.skinpackff.di

import id.candlekeeper.core.domain.AppInteractor
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.skinpackff.ui.detailSkin.DetailSkinViewModel
import id.candlekeeper.skinpackff.ui.home.HomeViewModel
import id.candlekeeper.skinpackff.ui.skinList.SkinsViewModel
import id.candlekeeper.skinpackff.ui.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<IAppUseCase> { AppInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { SkinsViewModel(get()) }
    viewModel { DetailSkinViewModel(get()) }
    viewModel { SplashViewModel(get()) }
}