package cz.applifting.graphqlempty.graphql

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.common.BaseViewModel
import cz.applifting.graphqlempty.common.Reducer
import cz.applifting.graphqlempty.ui.theme.apolloClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LaunchListViewModel: BaseViewModel<LaunchListState, LaunchListEvent, LaunchListAction>() {

    private val client = apolloClient


    override val reducer = LaunchListReducer(LaunchListState.initial())
    override val state: StateFlow<LaunchListState>
        get() = reducer.state

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