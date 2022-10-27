package cz.applifting.graphqlempty.firebase.chat

data class ChatMessage(
    val text: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val imageUrl: String? = null,
)