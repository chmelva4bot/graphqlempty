package cz.applifting.graphqlempty.graphql.launchDetail

import androidx.compose.runtime.Immutable
import cz.applifting.graphqlEmpty.LaunchDetailsQuery
import cz.applifting.graphqlempty.Action
import cz.applifting.graphqlempty.Event
import cz.applifting.graphqlempty.State

sealed class LaunchDetailEvent: cz.applifting.graphqlempty.Event {
    object ShowError: LaunchDetailEvent()
    object ShowLoading: LaunchDetailEvent()
    data class ShowData(val data: LaunchDetailsQuery.Data?): LaunchDetailEvent()

    data class UpdateTripsBooked(val count: Int): LaunchDetailEvent()
}

sealed class LaunchDetailAction: cz.applifting.graphqlempty.Action {
    data class FetchData(val id: String): LaunchDetailAction()
    data class BookTrip(val id: String): LaunchDetailAction()
    data class CancelTrip(val id: String): LaunchDetailAction()
}

@Immutable
data class LaunchDetailState(
    val isLoading: Boolean,
    val isError: Boolean,
    val data: LaunchDetailsQuery.Data?,
    val tripsBooked: Int
): cz.applifting.graphqlempty.State {
    companion object {
        fun initial() = LaunchDetailState(false, false, null, 0)
    }
}