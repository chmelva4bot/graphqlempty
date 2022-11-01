package cz.applifting.graphqlempty.firebase.chat.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DisplayChatUseCase(
    private val messagesDatabaseReference: DatabaseReference
) {

    fun messagesFlow(): Flow<List<ChatMessage>> = callbackFlow {
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
}