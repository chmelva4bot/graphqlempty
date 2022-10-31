package cz.applifting.graphqlempty.firebase.chat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import cz.applifting.graphqlempty.common.BaseViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//@HiltViewModel
class ChatViewModel constructor(

): BaseViewModel<ChatState, ChatEvent, ChatAction>() {

    private val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"

    override val reducer = ChatReducer(ChatState.initial())
    override val state: StateFlow<ChatState>
        get() = reducer.state

    private val db = Firebase.database
    private val msgs = db.reference.child("messages")

    private val _scrollToBottomEvent = MutableSharedFlow<Int>()
    val scrollToBottomEvent = _scrollToBottomEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            messagesFlow().collect {
                sendEvent(ChatEvent.UpdateMessages(it))
                _scrollToBottomEvent.emit(0)
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
                    val photoUrl = it.child("photoUrl").value as? String
                    val imageUrl = it.child("imageUrl").value as? String
                    ChatMessage(text, name, photoUrl, imageUrl)
                }
                trySend(res)
            }
        }
        msgs.addValueEventListener(valueListener)
        awaitClose {
            msgs.removeEventListener(valueListener)
        }
    }

    private fun sendMessage() {
        val msg = ChatMessage(
            text = state.value.msgText,
            name = Firebase.auth.currentUser?.displayName,
            photoUrl = Firebase.auth.currentUser?.photoUrl?.toString(),
            null
        )
        msgs.push().setValue(msg)
        sendEvent(ChatEvent.UpdateMsgText(""))
    }

    private fun sendImageMessage(uri: Uri) {
        viewModelScope.launch {
            val tmpMessage = ChatMessage(null, state.value.user?.displayName, state.value.user?.photoUrl.toString(), LOADING_IMAGE_URL)

            msgs.push().setValue(tmpMessage, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                if (databaseError != null) {
                    Log.w(
                        "ChatVM", "Unable to write message to database.",
                        databaseError.toException()
                    )
                    return@CompletionListener
                }

                // Build a StorageReference and then upload the file
                val key = databaseReference.key
                val storageReference = Firebase.storage
                    .getReference(state.value.user!!.uid)
                    .child(key!!)
                    .child(uri.lastPathSegment!!)
                putImageInStorage(storageReference, uri, key)
            })
        }
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
        storageReference.putFile(uri).addOnSuccessListener {taskSnapshot -> // After the image loads, get a public downloadUrl for the image
            // and add it to the message.
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    val friendlyMessage =
                        ChatMessage(null, state.value.user?.displayName, state.value.user?.photoUrl.toString(), uri.toString())
                    msgs
                        .child(key!!)
                        .setValue(friendlyMessage)
                }
        }
    }

    override fun handleAction(action: ChatAction) {
        when (action) {
            is ChatAction.UpdateMsgText -> {
                sendEvent(ChatEvent.UpdateMsgText(action.text))
            }
            is ChatAction.SendMessage -> sendMessage()
            is ChatAction.SendImageMessage -> sendImageMessage(action.uri)
            else -> checkUser()
        }
    }
}