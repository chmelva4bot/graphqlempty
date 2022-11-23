package cz.applifting.graphqlempty.firebasechat.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import cz.applifting.graphqlempty.firebasechat.login.ISignOutUserUseCase
import cz.applifting.graphqlempty.firebasechat.login.SignOutUserUseCase
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

    fun provideGetCurrentUserUseCase(auth: FirebaseAuth): cz.applifting.graphqlempty.firebasechat.auth.IGetCurrentUserUseCase =
        cz.applifting.graphqlempty.firebasechat.auth.GetCurrentUserUseCase(auth)

    fun provideMessageRepository(database: FirebaseDatabase): cz.applifting.graphqlempty.firebasechat.chat.data.IMessageRepository =
        cz.applifting.graphqlempty.firebasechat.chat.data.MessageRepository(database.reference.child("messages"))

    fun provideDisplayChatUseCase(messageRepository: cz.applifting.graphqlempty.firebasechat.chat.data.IMessageRepository): cz.applifting.graphqlempty.firebasechat.chat.data.DisplayChatUseCase {
        return cz.applifting.graphqlempty.firebasechat.chat.data.DisplayChatUseCase(messageRepository)
    }

    fun provideSendMessageUseCase(messageRepository: cz.applifting.graphqlempty.firebasechat.chat.data.IMessageRepository): cz.applifting.graphqlempty.firebasechat.chat.data.SendMessageUseCase {
        return cz.applifting.graphqlempty.firebasechat.chat.data.SendMessageUseCase(messageRepository)
    }

    fun provideUploadImageUseCase(storage: FirebaseStorage): cz.applifting.graphqlempty.firebasechat.chat.data.IUploadImageUseCase {
        return cz.applifting.graphqlempty.firebasechat.chat.data.UploadImageUseCase(storage)
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

    viewModel { cz.applifting.graphqlempty.firebasechat.chat.ui.ChatViewModel(get(), get(), get(), get()) }
}