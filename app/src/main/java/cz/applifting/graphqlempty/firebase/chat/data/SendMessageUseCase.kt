package cz.applifting.graphqlempty.firebase.chat.data

import android.util.Log
import com.google.firebase.database.DatabaseReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SendMessageUseCase (
    private val messagesDatabaseReference: DatabaseReference
){

    suspend fun sendMessage(message: ChatMessage): DatabaseReference = suspendCoroutine {

        messagesDatabaseReference.push().setValue(message) { error, ref ->
            if (error != null) it.resumeWithException(error.toException())
            else it.resume(ref)
        }
    }

    suspend fun replaceMessage(message: ChatMessage, key: String): DatabaseReference = suspendCoroutine {
        messagesDatabaseReference.child(key).setValue(message) { error, ref ->
            if (error != null) it.resumeWithException(error.toException())
            else it.resume(ref)
        }
    }
}