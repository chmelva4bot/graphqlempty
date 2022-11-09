package cz.applifting.graphqlempty.firebase.chat.data

import android.util.Log
import com.google.firebase.database.DatabaseReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SendMessageUseCase (
    private val repository: IMessageRepository
){
    suspend fun sendMessage(message: ChatMessage): String =  repository.sendMessage(message)

    suspend fun replaceMessage(message: ChatMessage, key: String): String  = repository.updateMessage(message, key)
}