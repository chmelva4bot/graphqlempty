package cz.applifting.graphqlempty.navigation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {


    viewModel { cz.applifting.graphqlempty.nav_common.OptionsMenuViewModel() }
}