package cz.applifting.graphqlempty.firebase.chat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//@HiltViewModel
class ChatViewModel constructor(

): BaseViewModel<ChatState, ChatEvent, ChatAction>() {

    private val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"

    override val reducer = ChatReducer(ChatState.initial())
    override val state: StateFlow<ChatState>
        get() = reducer.state

    private val db = FirebaseDatabase.getInstance("https://chm-cm-default-rtdb.europe-west1.firebasedatabase.app/")
    private val msgs = db.reference.child("messages")

    private val _scrollToBottomEvent = MutableSharedFlow<Int>()
    val scrollToBottomEvent = _scrollToBottomEvent.asSharedFlow()

    init {

    }

    private fun checkUser() {
        viewModelScope.launch {
            if (Firebase.auth.currentUser == null) {
                sendEvent(ChatEvent.AuthUser)
            } else {
                sendEvent(ChatEvent.SetUser(Firebase.auth.currentUser!!))
                collectMessages()
            }
        }
    }

    private fun collectMessages() {
        viewModelScope.launch {
            messagesFlow().collect {
                sendEvent(ChatEvent.UpdateMessages(it))
                _scrollToBottomEvent.emit(0)
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
        msgs.push().setValue(msg) { error, ref ->
            if (error != null) Log.e("ChatVM", "Msg upload failed", error.toException())
        }
        sendEvent(ChatEvent.UpdateMsgText(""))
    }

    private fun sendImageMessage(uri: Uri) {
        viewModelScope.launch {
            val tmpMessage = ChatMessage(null, state.value.user?.displayName, state.value.user?.photoUrl.toString(), LOADING_IMAGE_URL)

            val dbRef = uploadTempMessage(tmpMessage)

            // Build a StorageReference and then upload the file
            val key = dbRef.key
            val storageReference = Firebase.storage
                .getReference(state.value.user!!.uid)
                .child(key!!)
                .child(uri.lastPathSegment!!)

            val imgUri = putImageInStorage(storageReference, uri)

            val friendlyMessage =
                ChatMessage(null, state.value.user?.displayName, state.value.user?.photoUrl.toString(), imgUri.toString())
            msgs
                .child(key)
                .setValue(friendlyMessage)
        }
    }

    private suspend fun uploadTempMessage(message: ChatMessage): DatabaseReference  = suspendCoroutine {
        msgs.push().setValue(message) { error, ref ->
            if (error != null) it.resumeWithException(error.toException())
            else it.resume(ref)
        }
    }

    private suspend fun putImageInStorage(storageReference: StorageReference, uri: Uri): Uri = suspendCoroutine {
        storageReference.putFile(uri).addOnCompleteListener { taskSnapshot ->
            if (taskSnapshot.exception != null) {
                it.resumeWithException(taskSnapshot.exception!!)
            } else {
                taskSnapshot.result.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    it.resume(uri)
                }
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