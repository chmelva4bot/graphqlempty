package cz.applifting.graphqlempty.firebase.chat.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import cz.applifting.graphqlempty.common.BaseViewModel
import cz.applifting.graphqlempty.firebase.auth.GetCurrentUserUseCase
import cz.applifting.graphqlempty.firebase.chat.data.ChatMessage
import cz.applifting.graphqlempty.firebase.chat.data.DisplayChatUseCase
import cz.applifting.graphqlempty.firebase.chat.data.SendMessageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.UploadImageUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class ChatViewModel constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val displayChatUseCase: DisplayChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
): BaseViewModel<ChatState, ChatEvent, ChatAction>() {

    private val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"

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
                sendEvent(ChatEvent.UpdateMessages(it))
                _scrollToBottomEvent.emit(0)
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

            val messageDatabaseReference = sendMessageUseCase.sendMessage(tmpMessage)

            val uploadedImageUri = uploadImageUseCase.putImageInStorage(localImageUri, messageDatabaseReference,  state.value.user!!)

            val friendlyMessage =
                ChatMessage(null, state.value.user?.displayName, state.value.user?.photoUrl.toString(), uploadedImageUri.toString())

            sendMessageUseCase.replaceMessage(friendlyMessage, messageDatabaseReference.key!!)
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