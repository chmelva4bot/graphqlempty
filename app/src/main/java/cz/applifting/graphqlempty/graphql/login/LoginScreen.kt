package cz.applifting.graphqlempty.graphql.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cz.applifting.graphqlempty.graphql.launchDetail.LaunchDetailAction
import cz.applifting.graphqlempty.graphql.launchDetail.LaunchDetailState
import cz.applifting.graphqlempty.graphql.launchDetail.LaunchDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {

    val viewModel: LoginViewModel = hiltViewModel()
    var state by remember { mutableStateOf(LoginState.initial()) }
    val ctx = LocalContext.current

    LaunchedEffect(true) {
        this.launch {
            viewModel.state.collect { state = it}
        }
    }

    LaunchedEffect(state.isDone) {
        if (state.isDone) {
            User.setToken(ctx, state.token)
            navController.popBackStack()
        }
    }

    if (state.isLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    if (state.isError) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Oops there was an error during login!")
        }
    }



    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Log in", style = MaterialTheme.typography.h2, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = state.email, onValueChange = { viewModel.sendAction(LoginAction.UpdateEmail(it))}, label = {Text("Email")}, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { viewModel.sendAction(LoginAction.SubmitLogin)}, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Submit")
        }
    }

}