package cz.applifting.graphqlempty.graphql.launchDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.applifting.graphqlEmpty.LaunchDetailsQuery
import cz.applifting.graphqlempty.graphql.login.User
import cz.applifting.graphqlempty.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun LaunchDetailScreen(navController: NavController, id: String) {


    val viewModel: LaunchDetailViewModel = hiltViewModel()
    var state by remember { mutableStateOf(LaunchDetailState.initial())}

    LaunchedEffect(true) {
        this.launch {
            viewModel.state.collect { state = it}
        }
        viewModel.handleAction(LaunchDetailAction.FetchData(id))
    }

    if (state.isLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    if (state.isError) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Oops there was an error while loading data!")
        }
    }
   
    
    if (!state.isError && state.data != null) {

        val img = state.data?.launch?.mission?.missionPatch ?: ""
        val missionName = state.data?.launch?.mission?.name ?: ""
        val rocketName = state.data?.launch?.rocket?.name ?: ""
        val rocketType = state.data?.launch?.rocket?.type ?: ""
        val site = state.data?.launch?.site ?: ""

        val token = User.getToken(LocalContext.current)

        val btnText = if (token == null) "Log In" else if (state.data?.launch?.isBooked == true) "Cancel" else "Book Me"


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(Modifier.padding(horizontal = 0.dp, vertical = 48.dp), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = img, contentDescription = null, modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(text = missionName, style = MaterialTheme.typography.h6)
                    Text(text = "\uD83D\uDE80 $rocketName $rocketType", style = MaterialTheme.typography.body2)
                    Text(text = site, style = MaterialTheme.typography.caption)
                }
            }
            Button(
                onClick = {
                    if (token == null) {
                        navController.navigate(Screen.GraphQLLogin.route) {
                            popUpTo(Screen.GraphQLLogin.route) {
                                inclusive = true
                            }
                        }
                    } else if (state.data?.launch?.isBooked != true) {
                        viewModel.sendAction(LaunchDetailAction.BookTrip(id))
                    } else viewModel.sendAction(LaunchDetailAction.CancelTrip(id))
                },
                modifier = Modifier.fillMaxWidth()) {
                Text(text = btnText)
            }
        }
    }
}