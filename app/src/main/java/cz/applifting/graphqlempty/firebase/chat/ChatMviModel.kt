package cz.applifting.graphqlempty.firebase.chat

import androidx.compose.runtime.Immutable
import com.google.firebase.auth.FirebaseUser
import cz.applifting.graphqlempty.common.Action
import cz.applifting.graphqlempty.common.Event
import cz.applifting.graphqlempty.common.State

sealed class ChatEvent: Event {
    object ShowError: ChatEvent()
    object ShowLoading: ChatEvent()
    object AuthUser: ChatEvent()

    data class UpdateMessages(val msgs: List<ChatMessage>): ChatEvent()
}

sealed class ChatAction: Action {
    object FetchData: ChatAction()
}

@Immutable
data class ChatState(
    val isLoading: Boolean,
    val isError: Boolean,
    val user: FirebaseUser?,
    val isUserChecked: Boolean,
    val messages: List<ChatMessage>,
): State {
    companion object {
        fun initial() = ChatState(false, false, null, false, listOf())
    }
}