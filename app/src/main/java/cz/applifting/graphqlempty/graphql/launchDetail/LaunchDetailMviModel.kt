package cz.applifting.graphqlempty.graphql.launchDetail

import androidx.compose.runtime.Immutable
import cz.applifting.graphqlEmpty.LaunchDetailsQuery
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.common.Action
import cz.applifting.graphqlempty.common.Event
import cz.applifting.graphqlempty.common.State

sealed class LaunchDetailEvent: Event {
    object ShowError: LaunchDetailEvent()
    object ShowLoading: LaunchDetailEvent()
    data class ShowData(val data: LaunchDetailsQuery.Data?): LaunchDetailEvent()
}

sealed class LaunchDetailAction: Action {
    data class FetchData(val id: String): LaunchDetailAction()
}

@Immutable
data class LaunchDetailState(
    val isLoading: Boolean,
    val isError: Boolean,
    val data: LaunchDetailsQuery.Data?
): State {
    companion object {
        fun initial() = LaunchDetailState(false, false, null)
    }
}