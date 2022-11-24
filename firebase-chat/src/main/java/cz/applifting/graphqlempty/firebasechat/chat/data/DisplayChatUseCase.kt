package cz.applifting.graphqlempty.firebasechat.chat.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DisplayChatUseCase(
    private val repository: IMessageRepository
) {
    fun messagesFlow(): Flow<List<ChatMessage>> = repository.getMessages()
}