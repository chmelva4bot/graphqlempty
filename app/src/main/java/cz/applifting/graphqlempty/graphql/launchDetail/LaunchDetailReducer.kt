package cz.applifting.graphqlempty.graphql.launchDetail

import cz.applifting.graphqlempty.common.Reducer

class LaunchDetailReducer(initialState: LaunchDetailState): Reducer<LaunchDetailState, LaunchDetailEvent>(initialState) {
    override fun reduce(oldState: LaunchDetailState, event: LaunchDetailEvent) {
        when (event) {
            is LaunchDetailEvent.ShowError -> {
                setState(oldState.copy(isLoading = false, isError = true))
            }
            is LaunchDetailEvent.ShowLoading -> {
                setState(oldState.copy(isLoading = true, isError = false))
            }
            is LaunchDetailEvent.ShowData -> {
                setState(oldState.copy(data = event.data, isError = false, isLoading = false))
            }
            is LaunchDetailEvent.UpdateTripsBooked -> {
                setState(oldState.copy(tripsBooked = event.count))
            }
        }
    }
}