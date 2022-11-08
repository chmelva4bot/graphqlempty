package cz.applifting.graphqlempty.firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import cz.applifting.graphqlempty.firebase.auth.GetCurrentUserUseCase
import cz.applifting.graphqlempty.firebase.auth.IGetCurrentUserUseCase
import cz.applifting.graphqlempty.firebase.chat.data.DisplayChatUseCase
import cz.applifting.graphqlempty.firebase.chat.data.IMessageRepository
import cz.applifting.graphqlempty.firebase.chat.data.IUploadImageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.MessageRepository
import cz.applifting.graphqlempty.firebase.chat.data.SendMessageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.UploadImageUseCase
import cz.applifting.graphqlempty.firebase.chat.ui.ChatViewModel
import cz.applifting.graphqlempty.firebase.login.ISignOutUserUseCase
import cz.applifting.graphqlempty.firebase.login.SignOutUserUseCase
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

    fun provideGetCurrentUserUseCase(auth: FirebaseAuth): IGetCurrentUserUseCase = GetCurrentUserUseCase(auth)

    fun provideMessageRepository(database: FirebaseDatabase): IMessageRepository = MessageRepository(database.reference.child("messages"))

    fun provideDisplayChatUseCase(messageRepository: IMessageRepository): DisplayChatUseCase {
        return DisplayChatUseCase(messageRepository)
    }

    fun provideSendMessageUseCase(messageRepository: IMessageRepository): SendMessageUseCase {
        return SendMessageUseCase(messageRepository)
    }

    fun provideUploadImageUseCase(storage: FirebaseStorage): IUploadImageUseCase {
        return UploadImageUseCase(storage)
    }

    fun provideSignOutUserUseCase(auth: FirebaseAuth): ISignOutUserUseCase = SignOutUserUseCase(auth)

    single { provideFirebaseDatabase() }
    single { provideFirebaseStorage() }
    single { provideFirebaseAuth() }
    single { provideMessageRepository(get()) }
    single { provideGetCurrentUserUseCase(get()) }
    single { provideDisplayChatUseCase(get()) }
    single { provideSendMessageUseCase(get()) }
    single { provideUploadImageUseCase(get()) }
    single { provideSignOutUserUseCase(get()) }

    viewModel { ChatViewModel(get(), get(), get(), get()) }
}