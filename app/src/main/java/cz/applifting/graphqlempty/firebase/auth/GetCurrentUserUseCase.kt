package cz.applifting.graphqlempty.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

class GetCurrentUserUseCase(
    private val auth: FirebaseAuth
) {
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun observeUser(): Flow<FirebaseUser?> = callbackFlow {
        trySend(auth.currentUser)

        val listener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser)
        }

        auth.addAuthStateListener(listener)

        awaitClose { auth.removeAuthStateListener(listener) }
    }
}