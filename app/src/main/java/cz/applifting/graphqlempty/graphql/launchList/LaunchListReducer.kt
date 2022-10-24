package cz.applifting.graphqlempty.graphql.launchList

import cz.applifting.graphqlempty.common.Reducer

class LaunchListReducer(initialState: LaunchListState): Reducer<LaunchListState, LaunchListEvent>(initialState) {
    override fun reduce(oldState: LaunchListState, event: LaunchListEvent) {
        when (event) {
            is LaunchListEvent.ShowError -> {
                setState(oldState.copy(isLoading = false, isError = true))
            }
            is LaunchListEvent.ShowLoading -> {
                setState(oldState.copy(isLoading = true, isError = false))
            }
            is LaunchListEvent.ShowData -> {
                setState(oldState.copy(data = event.data, cursor = event.cursor, hasMore = event.hasMore))
            }
        }
    }
}