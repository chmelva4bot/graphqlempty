package cz.applifting.graphqlempty.firebase.chat.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MessageRepositoryFake: IMessageRepository {

    val messages = MutableStateFlow<List<ChatMessage>>(emptyList())

    override fun getMessages(): Flow<List<ChatMessage>> = messages

    override suspend fun sendMessage(message: ChatMessage): String {
        val current = messages.value.toMutableList()
        current.add(message)
        messages.tryEmit(current)
        return "mkey1"
    }

    override suspend fun updateMessage(message: ChatMessage, key: String): String {
        TODO("Not yet implemented")
    }
}