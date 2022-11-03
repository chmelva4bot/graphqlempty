package cz.applifting.graphqlempty.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface IGetCurrentUserUseCase {
    fun observeUser(): Flow<BasicUser?>
}

class GetCurrentUserUseCase(
    private val auth: FirebaseAuth
) : IGetCurrentUserUseCase {
    private fun getCurrentUser(): BasicUser? {
        val user = auth.currentUser ?: return null
        return BasicUser(user.uid, user.displayName?: "Display name", user.photoUrl?.toString())
    }

    override fun observeUser(): Flow<BasicUser?> = callbackFlow {
        trySend(getCurrentUser())

        val listener = FirebaseAuth.AuthStateListener {
            trySend(getCurrentUser())
        }

        auth.addAuthStateListener(listener)

        awaitClose { auth.removeAuthStateListener(listener) }
    }
}