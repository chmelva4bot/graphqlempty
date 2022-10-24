package cz.applifting.graphqlempty.graphql.launchDetail

import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import cz.applifting.graphqlEmpty.LaunchDetailsQuery
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.common.BaseViewModel
import cz.applifting.graphqlempty.ui.theme.apolloClient
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LaunchDetailViewModel: BaseViewModel<LaunchDetailState, LaunchDetailEvent, LaunchDetailAction>() {

    private val client = apolloClient


    override val reducer = LaunchDetailReducer(LaunchDetailState.initial())
    override val state: StateFlow<LaunchDetailState>
        get() = reducer.state


    init {
    }

    override fun handleAction(action: LaunchDetailAction) {
        when(action) {
            is LaunchDetailAction.FetchData -> fetchData(action.id)
        }
    }

    private fun fetchData(id: String) {
        viewModelScope.launch {
            sendEvent(LaunchDetailEvent.ShowLoading)
            try {
                val response = client.query(LaunchDetailsQuery(id)).execute()
                sendEvent(
                    LaunchDetailEvent.ShowData(
                        data = response.data
                    )
                )

            } catch (e: Exception) {
               sendEvent(LaunchDetailEvent.ShowError)
            }




        }
    }
}