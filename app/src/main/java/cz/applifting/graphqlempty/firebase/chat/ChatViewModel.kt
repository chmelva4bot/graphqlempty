package cz.applifting.graphqlempty.firebase.chat

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import cz.applifting.graphqlempty.common.BaseViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class ChatViewModel constructor(

): BaseViewModel<ChatState, ChatEvent, ChatAction>() {

    override val reducer = ChatReducer(ChatState.initial())
    override val state: StateFlow<ChatState>
        get() = reducer.state


    init {
        viewModelScope.launch {
            if (Firebase.auth.currentUser == null) {
                sendEvent(ChatEvent.AuthUser)
            }
        }
    }

    override fun handleAction(action: ChatAction) {

    }
}