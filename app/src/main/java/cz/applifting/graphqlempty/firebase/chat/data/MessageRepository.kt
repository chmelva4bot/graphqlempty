package cz.applifting.graphqlempty.firebase.chat.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface IMessageRepository {
    fun getMessages(): Flow<List<ChatMessage>>

    suspend fun sendMessage(message: ChatMessage): String

    suspend fun updateMessage(message: ChatMessage, key: String): String
}

class MessageRepository(
    private val messagesDatabaseReference: DatabaseReference
) : IMessageRepository {

    override fun getMessages(): Flow<List<ChatMessage>>  = callbackFlow {
        val valueListener = object: ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                close()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val res = snapshot.children.map {
                    val text = it.child("text").value as? String
                    val name = it.child("name").value as? String
                    val photoUrl = it.child("photoUrl").value as? String
                    val imageUrl = it.child("imageUrl").value as? String
                    ChatMessage(text, name, photoUrl, imageUrl)
                }
                trySend(res)
            }
        }
        messagesDatabaseReference.addValueEventListener(valueListener)
        awaitClose {
            messagesDatabaseReference.removeEventListener(valueListener)
        }
    }

    override suspend fun sendMessage(message: ChatMessage): String = suspendCoroutine {
        messagesDatabaseReference.push().setValue(message) { error, ref ->
            if (error != null) it.resumeWithException(error.toException())
            else it.resume(ref.key!!)
        }
    }

    override suspend fun updateMessage(message: ChatMessage, key: String): String = suspendCoroutine {
        messagesDatabaseReference.child(key).setValue(message) { error, ref ->
            if (error != null) it.resumeWithException(error.toException())
            else it.resume(ref.key!!)
        }
    }

}