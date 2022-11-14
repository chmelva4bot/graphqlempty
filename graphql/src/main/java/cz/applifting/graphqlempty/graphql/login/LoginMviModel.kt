package cz.applifting.graphqlempty.graphql.login

import androidx.compose.runtime.Immutable
import cz.applifting.graphqlempty.Action
import cz.applifting.graphqlempty.Event
import cz.applifting.graphqlempty.State

sealed class LoginEvent: cz.applifting.graphqlempty.Event {
    object ShowError: LoginEvent()
    object ShowLoading: LoginEvent()
    data class UpdateEmail(val fieldValue: String): LoginEvent()
    data class LeaveScreen(val token: String): LoginEvent()
}

sealed class LoginAction: cz.applifting.graphqlempty.Action {
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
): cz.applifting.graphqlempty.State {
    companion object {
        fun initial() = LoginState(false, false, false, "", "")
    }
}