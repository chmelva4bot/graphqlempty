package cz.applifting.graphqlempty.graphql.launchList

import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchListViewModel @Inject constructor(
    private val client: ApolloClient
): BaseViewModel<LaunchListState, LaunchListEvent, LaunchListAction>() {

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