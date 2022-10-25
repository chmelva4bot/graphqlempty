package cz.applifting.graphqlempty.graphql.login

import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import cz.applifting.graphqlEmpty.LaunchDetailsQuery
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlEmpty.LoginMutation
import cz.applifting.graphqlempty.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ApolloClient
): BaseViewModel<LoginState, LoginEvent, LoginAction>() {

    override val reducer = LoginReducer(LoginState.initial())
    override val state: StateFlow<LoginState>
        get() = reducer.state


    init {
    }

    override fun handleAction(action: LoginAction) {
        when(action) {
            is LoginAction.UpdateEmail -> sendEvent(LoginEvent.UpdateEmail(action.email))
            is LoginAction.SubmitLogin -> submitLogin()
        }
    }

    private fun submitLogin() {
        viewModelScope.launch {
            sendEvent(LoginEvent.ShowLoading)
            delay(1000)
            try {
                val response = client.mutation(LoginMutation(email = Optional.present(state.value.email))).execute()
                val token = response.data?.login?.token
                if (token == null || response.hasErrors()) sendEvent(LoginEvent.ShowError)
                else sendEvent(
                    LoginEvent.LeaveScreen(token)
                )
            } catch (e: Exception) {
               sendEvent(LoginEvent.ShowError)
            }
        }
    }
}