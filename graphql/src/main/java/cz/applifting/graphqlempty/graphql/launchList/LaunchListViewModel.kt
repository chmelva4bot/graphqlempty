package cz.applifting.graphqlempty.graphql.launchList

import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class LaunchListViewModel constructor(
    private val client: ApolloClient
): cz.applifting.graphqlempty.BaseViewModel<LaunchListState, LaunchListEvent, LaunchListAction>() {

    override val reducer = LaunchListReducer(LaunchListState.initial())
    override val state: StateFlow<LaunchListState>
        get() = reducer.state


    init {
        viewModelScope.launch {
            handleAction(LaunchListAction.FetchData)
        }
    }

    override fun handleAction(action: LaunchListAction) {
        when(action) {
            is LaunchListAction.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            sendEvent(LaunchListEvent.ShowLoading)
            try {
                delay(1000)
                val response = client.query(LaunchListQuery(Optional.Present(state.value.cursor))).execute()
                val newLaunches = response.data?.launches?.launches?.filterNotNull() ?: listOf()

                sendEvent(
                    LaunchListEvent.ShowData(
                        data = state.value.data + newLaunches,
                        cursor = response.data?.launches?.cursor,
                        hasMore = response.data?.launches?.hasMore ?: true
                    )
                )

            } catch (e: Exception) {
               sendEvent(LaunchListEvent.ShowError)
            }
        }
    }
}