package cz.applifting.graphqlempty.firebase.di

import cz.applifting.graphqlempty.firebase.chat.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseModule = module {

    viewModel { ChatViewModel() }
}