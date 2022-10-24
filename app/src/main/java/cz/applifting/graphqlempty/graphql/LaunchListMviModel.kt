package cz.applifting.graphqlempty.graphql

import androidx.compose.runtime.Immutable
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.common.Action
import cz.applifting.graphqlempty.common.Event
import cz.applifting.graphqlempty.common.State

sealed class LaunchListEvent: Event {
    object ShowError: LaunchListEvent()
    object ShowLoading: LaunchListEvent()
    data class ShowData(val data: List<LaunchListQuery.Launch>, val cursor: String?, val hasMore: Boolean): LaunchListEvent()
}

sealed class LaunchListAction: Action {
    object FetchData: LaunchListAction()
}

@Immutable
data class LaunchListState(
    val isLoading: Boolean,
    val isError: Boolean,
    val hasMore: Boolean,
    val cursor: String?,
    val data: List<LaunchListQuery.Launch>
): State {
    companion object {
        fun initial() = LaunchListState(false, false, false, null, listOf())
    }
}