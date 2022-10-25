package cz.applifting.graphqlempty.graphql.login

import cz.applifting.graphqlempty.common.Reducer

class LoginReducer(initialState: LoginState): Reducer<LoginState, LoginEvent>(initialState) {
    override fun reduce(oldState: LoginState, event: LoginEvent) {
        when (event) {
            is LoginEvent.ShowError -> {
                setState(oldState.copy(isLoading = false, isError = true))
            }
            is LoginEvent.ShowLoading -> {
                setState(oldState.copy(isLoading = true, isError = false))
            }
            is LoginEvent.UpdateEmail -> {
                setState(oldState.copy(email = event.fieldValue))
            }
            is LoginEvent.LeaveScreen -> {
                setState(oldState.copy(isDone = true, isLoading = false, token = event.token))
            }
        }
    }
}