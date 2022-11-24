package cz.applifting.graphqlempty.firebasechat.login

import com.google.firebase.auth.FirebaseAuth

interface ISignOutUserUseCase {
    fun signOut()
}

class SignOutUserUseCase(
    private val auth: FirebaseAuth
) : ISignOutUserUseCase {

    override fun signOut() {
        auth.signOut()
    }
}