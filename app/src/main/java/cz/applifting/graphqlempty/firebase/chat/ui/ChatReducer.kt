package cz.applifting.graphqlempty.firebase.chat.ui

import cz.applifting.graphqlempty.common.Reducer

class ChatReducer(initialState: ChatState): Reducer<ChatState, ChatEvent>(initialState) {
    override fun reduce(oldState: ChatState, event: ChatEvent) {
        when (event) {
            is ChatEvent.ShowError -> {
                setState(oldState.copy(isLoading = false, isError = true))
            }
            is ChatEvent.ShowLoading -> {
                setState(oldState.copy(isLoading = true, isError = false))
            }
            is ChatEvent.AuthUser -> {
                setState(oldState.copy(isUserChecked = true, user = null))
            }
            is ChatEvent.UpdateMessages -> {
                setState(oldState.copy(messages = event.msgs))
            }
            is ChatEvent.SetUser -> {
                setState(oldState.copy(user = event.user))
            }
            is ChatEvent.UpdateMsgText -> {
                setState(oldState.copy(msgText = event.text))
            }
        }
    }
}