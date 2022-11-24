package cz.applifting.graphqlempty.graphql.launchList

import androidx.compose.runtime.Immutable
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.Action
import cz.applifting.graphqlempty.Event
import cz.applifting.graphqlempty.State

sealed class LaunchListEvent: cz.applifting.graphqlempty.Event {
    object ShowError: LaunchListEvent()
    object ShowLoading: LaunchListEvent()
    data class ShowData(val data: List<LaunchListQuery.Launch>, val cursor: String?, val hasMore: Boolean): LaunchListEvent()
}

sealed class LaunchListAction: cz.applifting.graphqlempty.Action {
    object FetchData: LaunchListAction()
}

@Immutable
data class LaunchListState(
    val isLoading: Boolean,
    val isError: Boolean,
    val hasMore: Boolean,
    val cursor: String?,
    val data: List<LaunchListQuery.Launch>
): cz.applifting.graphqlempty.State {
    companion object {
        fun initial() = LaunchListState(false, false, false, null, listOf())
    }
}