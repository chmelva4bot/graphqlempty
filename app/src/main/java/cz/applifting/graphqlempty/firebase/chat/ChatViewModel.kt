package cz.applifting.graphqlempty.firebase.chat

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import cz.applifting.graphqlempty.common.BaseViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//@HiltViewModel
class ChatViewModel constructor(

): BaseViewModel<ChatState, ChatEvent, ChatAction>() {

    override val reducer = ChatReducer(ChatState.initial())
    override val state: StateFlow<ChatState>
        get() = reducer.state

    private val db = Firebase.database
    private val msgs = db.reference.child("messages")

    init {
        viewModelScope.launch {
            messagesFlow().collect {
                sendEvent(ChatEvent.UpdateMessages(it))
            }
        }
    }

    private fun checkUser() {
        viewModelScope.launch {
            if (Firebase.auth.currentUser == null) {
                sendEvent(ChatEvent.AuthUser)
            } else {
                sendEvent(ChatEvent.SetUser(Firebase.auth.currentUser!!))
            }
        }
    }

    private fun messagesFlow(): Flow<List<ChatMessage>> = callbackFlow {
        val valueListener = object: ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                close()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val res = snapshot.children.map {
                    val text = it.child("text").value as? String
                    val name = it.child("name").value as? String
                    ChatMessage(text, name)
                }
                trySend(res)
            }
        }

        msgs.addValueEventListener(valueListener)


        awaitClose {
            msgs.removeEventListener(valueListener)
        }
    }

    override fun handleAction(action: ChatAction) {
        when (action) {

            else -> checkUser()
        }
    }
}