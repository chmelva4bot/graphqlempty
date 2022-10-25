package cz.applifting.graphqlempty.graphql.login

import androidx.compose.runtime.Immutable
import cz.applifting.graphqlempty.common.Action
import cz.applifting.graphqlempty.common.Event
import cz.applifting.graphqlempty.common.State

sealed class LoginEvent: Event {
    object ShowError: LoginEvent()
    object ShowLoading: LoginEvent()
    data class UpdateEmail(val fieldValue: String): LoginEvent()
    data class LeaveScreen(val token: String): LoginEvent()
}

sealed class LoginAction: Action {
    data class UpdateEmail(val email: String): LoginAction()
    object SubmitLogin: LoginAction()
}

@Immutable
data class LoginState(
    val isLoading: Boolean,
    val isError: Boolean,
    val isDone: Boolean,
    val token: String,
    val email: String
): State {
    companion object {
        fun initial() = LoginState(false, false, false, "", "")
    }
}