package cz.applifting.graphqlempty.firebasechat.chat.data

import android.net.Uri
import cz.applifting.graphqlempty.firebasechat.auth.BasicUser
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UploadImageUseCaseFake: IUploadImageUseCase {
    override suspend fun putImageInStorage(imageUri: Uri, messageKey: String, user: cz.applifting.graphqlempty.firebasechat.auth.BasicUser): Uri = suspendCoroutine {

        it.resume(imageUri)
    }
}