package cz.applifting.graphqlempty.firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import cz.applifting.graphqlempty.firebase.auth.GetCurrentUserUseCase
import cz.applifting.graphqlempty.firebase.chat.data.DisplayChatUseCase
import cz.applifting.graphqlempty.firebase.chat.data.SendMessageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.UploadImageUseCase
import cz.applifting.graphqlempty.firebase.chat.ui.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseModule = module {

    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance("https://chm-cm-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }

    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    fun provideGetCurrentUserUseCase(auth: FirebaseAuth): GetCurrentUserUseCase = GetCurrentUserUseCase(auth)

    fun provideDisplayChatUseCase(database: FirebaseDatabase): DisplayChatUseCase {
        return DisplayChatUseCase(database.reference.child("messages"))
    }

    fun provideSendMessageUseCase(database: FirebaseDatabase): SendMessageUseCase {
        return SendMessageUseCase(database.reference.child("messages"))
    }

    fun provideUploadImageUseCase(storage: FirebaseStorage): UploadImageUseCase {
        return UploadImageUseCase(storage)
    }

    single { provideFirebaseDatabase() }
    single { provideFirebaseStorage() }
    single { provideFirebaseAuth() }
    single { provideGetCurrentUserUseCase(get()) }
    single { provideDisplayChatUseCase(get()) }
    single { provideSendMessageUseCase(get()) }
    single { provideUploadImageUseCase(get()) }

    viewModel { ChatViewModel(get(), get(), get(), get()) }
}