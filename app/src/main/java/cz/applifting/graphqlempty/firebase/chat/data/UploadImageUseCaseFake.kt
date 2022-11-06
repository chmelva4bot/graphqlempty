package cz.applifting.graphqlempty.firebase.chat.data

import android.net.Uri
import cz.applifting.graphqlempty.firebase.auth.BasicUser
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UploadImageUseCaseFake: IUploadImageUseCase {
    override suspend fun putImageInStorage(imageUri: Uri, messageKey: String, user: BasicUser): Uri = suspendCoroutine {

        it.resume(imageUri)
    }
}