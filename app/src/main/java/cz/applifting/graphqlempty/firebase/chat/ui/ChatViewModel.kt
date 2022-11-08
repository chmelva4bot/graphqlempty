package cz.applifting.graphqlempty.firebase.chat.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import cz.applifting.graphqlempty.common.BaseViewModel
import cz.applifting.graphqlempty.firebase.auth.GetCurrentUserUseCase
import cz.applifting.graphqlempty.firebase.auth.IGetCurrentUserUseCase
import cz.applifting.graphqlempty.firebase.chat.data.ChatMessage
import cz.applifting.graphqlempty.firebase.chat.data.DisplayChatUseCase
import cz.applifting.graphqlempty.firebase.chat.data.IUploadImageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.SendMessageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.UploadImageUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class ChatViewModel constructor(
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
    private val displayChatUseCase: DisplayChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val uploadImageUseCase: IUploadImageUseCase,
): BaseViewModel<ChatState, ChatEvent, ChatAction>() {

    companion object {
        const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }


    override val reducer = ChatReducer(ChatState.initial())
    override val state: StateFlow<ChatState>
        get() = reducer.state


    private val _scrollToBottomEvent = MutableSharedFlow<Int>()
    val scrollToBottomEvent = _scrollToBottomEvent.asSharedFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            getCurrentUserUseCase.observeUser().collect { newUser ->
//                Log.d("ChatVM", "Current user: ${newUser?.uid}")
                if (newUser == null) {
//                    sendEvent(ChatEvent.SetUser(null))
                    sendEvent(ChatEvent.AuthUser)
                } else {
                    sendEvent(ChatEvent.SetUser(newUser))
                    collectMessages()
                }
            }
        }
    }

    private fun collectMessages() {
        viewModelScope.launch {
            displayChatUseCase.messagesFlow().collect {
                sendEvent(ChatEvent.UpdateMessages(it.reversed()))
            }
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            val msg = ChatMessage(
                text = state.value.msgText,
                name = state.value.user?.displayName,
                photoUrl = state.value.user?.photoUrl?.toString(),
                null
            )
            sendMessageUseCase.sendMessage(msg)
            sendEvent(ChatEvent.UpdateMsgText(""))
        }
    }

    private fun sendImageMessage(localImageUri: Uri) {
        viewModelScope.launch {
            val tmpMessage = ChatMessage(null, state.value.user?.displayName, state.value.user?.photoUrl.toString(), LOADING_IMAGE_URL)

            val messageKey = sendMessageUseCase.sendMessage(tmpMessage)

            val uploadedImageUri = uploadImageUseCase.putImageInStorage(localImageUri, messageKey,  state.value.user!!)

            val friendlyMessage =
                ChatMessage(null, state.value.user?.displayName, state.value.user?.photoUrl.toString(), uploadedImageUri.toString())

            sendMessageUseCase.replaceMessage(friendlyMessage, messageKey)
        }
    }

    override fun handleAction(action: ChatAction) {
        when (action) {
            is ChatAction.UpdateMsgText -> {
                sendEvent(ChatEvent.UpdateMsgText(action.text))
            }
            is ChatAction.SendMessage -> sendMessage()
            is ChatAction.SendImageMessage -> sendImageMessage(action.uri)
            is ChatAction.CheckUser -> {}
        }
    }
}