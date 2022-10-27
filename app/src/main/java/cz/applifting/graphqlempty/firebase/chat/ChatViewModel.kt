package cz.applifting.graphqlempty.firebase.chat

import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(

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