package cz.applifting.graphqlempty.firebase.chat.data

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import cz.applifting.graphqlempty.firebase.auth.BasicUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface IUploadImageUseCase {
    suspend fun putImageInStorage(imageUri: Uri, messageKey: String, user: BasicUser): Uri
}

class UploadImageUseCase(
    private val storage: FirebaseStorage
) : IUploadImageUseCase {

    private fun buildStorageReference(userUid: String, messageKey: String, imageUri: Uri): StorageReference {
        return storage.getReference(userUid).child(messageKey).child(imageUri.lastPathSegment!!)
    }

    override suspend fun putImageInStorage(imageUri: Uri, messageKey: String, user: BasicUser): Uri = suspendCoroutine {
        val storageReference = buildStorageReference(user.uid, messageKey, imageUri)
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