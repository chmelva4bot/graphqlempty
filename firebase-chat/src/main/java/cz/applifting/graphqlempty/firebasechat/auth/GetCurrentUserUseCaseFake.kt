package cz.applifting.graphqlempty.firebasechat.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class GetCurrentUserUseCaseFake: IGetCurrentUserUseCase {

    val userFlow = MutableSharedFlow<BasicUser?>()

    override fun observeUser(): Flow<BasicUser?> = userFlow
}