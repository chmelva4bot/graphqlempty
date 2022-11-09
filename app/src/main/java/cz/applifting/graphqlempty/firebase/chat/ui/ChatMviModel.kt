package cz.applifting.graphqlempty.firebase.chat.ui

import android.net.Uri
import androidx.compose.runtime.Immutable
import cz.applifting.graphqlempty.Action
import cz.applifting.graphqlempty.Event
import cz.applifting.graphqlempty.State
import cz.applifting.graphqlempty.firebase.auth.BasicUser
import cz.applifting.graphqlempty.firebase.chat.data.ChatMessage

sealed class ChatEvent: cz.applifting.graphqlempty.Event {
    object ShowError: ChatEvent()
    object ShowLoading: ChatEvent()
    object AuthUser: ChatEvent()
    data class SetUser(val user: BasicUser): ChatEvent()

    data class UpdateMessages(val msgs: List<ChatMessage>): ChatEvent()
    data class UpdateMsgText(val text: String): ChatEvent()
}

sealed class ChatAction: cz.applifting.graphqlempty.Action {
    object CheckUser: ChatAction()
    data class UpdateMsgText(val text: String): ChatAction()
    object SendMessage: ChatAction()

    data class SendImageMessage(val uri: Uri): ChatAction()
}

@Immutable
data class ChatState(
    val isLoading: Boolean,
    val isError: Boolean,
    val user: BasicUser?,
    val isUserChecked: Boolean,
    val messages: List<ChatMessage>,
    val msgText: String
): cz.applifting.graphqlempty.State {
    companion object {
        fun initial() = ChatState(false, false, null, false, listOf(), "")
    }
}