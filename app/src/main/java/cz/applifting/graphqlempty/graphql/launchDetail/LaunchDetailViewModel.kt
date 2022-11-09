package cz.applifting.graphqlempty.graphql.launchDetail

import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import cz.applifting.graphqlEmpty.BookTripMutation
import cz.applifting.graphqlEmpty.CancelTripMutation
import cz.applifting.graphqlEmpty.LaunchDetailsQuery
import cz.applifting.graphqlEmpty.TripsBookedSubscription
import cz.applifting.graphqlempty.common.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class LaunchDetailViewModel constructor(
    private val client: ApolloClient
) : BaseViewModel<LaunchDetailState, LaunchDetailEvent, LaunchDetailAction>() {

    override val reducer = LaunchDetailReducer(LaunchDetailState.initial())
    override val state: StateFlow<LaunchDetailState>
        get() = reducer.state

    init {
        viewModelScope.launch {
            client.subscription(TripsBookedSubscription()).toFlow().collect {
                val count = it.data?.tripsBooked ?: 0
                sendEvent(LaunchDetailEvent.UpdateTripsBooked(count))
            }
        }
    }

    override fun handleAction(action: LaunchDetailAction) {
        when(action) {
            is LaunchDetailAction.FetchData -> fetchData(action.id)
            is LaunchDetailAction.BookTrip -> bookTrip(action.id)
            is LaunchDetailAction.CancelTrip -> cancelTrip(action.id)
        }
    }

    private fun fetchData(id: String) {
        viewModelScope.launch {
            sendEvent(LaunchDetailEvent.ShowLoading)
            delay(1000)
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

    private fun bookTrip(id: String) {
        viewModelScope.launch {
            sendEvent(LaunchDetailEvent.ShowLoading)
            delay(1000)
            try {
                val response = client.mutation(BookTripMutation(id)).execute()
                if (response.data?.bookTrips?.success == true) {
                    fetchData(id)
                }
            } catch (e: Exception) {
                sendEvent(LaunchDetailEvent.ShowError)
            }
        }
    }

    private fun cancelTrip(id: String) {
        viewModelScope.launch {
            sendEvent(LaunchDetailEvent.ShowLoading)
            delay(1000)
            try {
                val response = client.mutation(CancelTripMutation(id)).execute()
                if (response.data?.cancelTrip?.success == true) {
                    fetchData(id)
                }
            } catch (e: Exception) {
                sendEvent(LaunchDetailEvent.ShowError)
            }
        }
    }
}