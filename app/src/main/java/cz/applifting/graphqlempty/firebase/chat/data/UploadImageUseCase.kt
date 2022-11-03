package cz.applifting.graphqlempty.firebase.chat.data

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UploadImageUseCase(
    private val storage: FirebaseStorage
){

    private fun buildStorageReference(user: FirebaseUser, messageKey: String, imageUri: Uri): StorageReference {
        return storage.getReference(user.uid).child(messageKey).child(imageUri.lastPathSegment!!)
    }

    suspend fun putImageInStorage(imageUri: Uri, messageKey: String, user: FirebaseUser): Uri = suspendCoroutine {
        val storageReference = buildStorageReference(user, messageKey, imageUri)
        storageReference.putFile(imageUri).addOnCompleteListener { taskSnapshot ->
            if (taskSnapshot.exception != null) {
                it.resumeWithException(taskSnapshot.exception!!)
            } else {
                taskSnapshot.result.metadata!!.reference!!.downloadUrl.addOnSuccessListener { publicUri ->
                    it.resume(publicUri)
                }
            }
        }
    }
}