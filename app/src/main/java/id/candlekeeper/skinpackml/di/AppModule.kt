package id.candlekeeper.skinpackml.di

import id.candlekeeper.core.domain.AppInteractor
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.skinpackml.ui.detailSkin.DetailSkinViewModel
import id.candlekeeper.skinpackml.ui.heroList.HeroViewModel
import id.candlekeeper.skinpackml.ui.home.HomeViewModel
import id.candlekeeper.skinpackml.ui.skinList.SkinsViewModel
import id.candlekeeper.skinpackml.ui.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<IAppUseCase> { AppInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { HeroViewModel(get()) }
    viewModel { SkinsViewModel(get()) }
    viewModel { DetailSkinViewModel(get()) }
    viewModel { SplashViewModel(get()) }
}