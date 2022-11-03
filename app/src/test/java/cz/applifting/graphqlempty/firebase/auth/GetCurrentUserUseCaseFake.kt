package cz.applifting.graphqlempty.firebase.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class GetCurrentUserUseCaseFake: IGetCurrentUserUseCase {

    val userFlow = MutableSharedFlow<BasicUser?>()

    override fun observeUser(): Flow<BasicUser?> = userFlow
}