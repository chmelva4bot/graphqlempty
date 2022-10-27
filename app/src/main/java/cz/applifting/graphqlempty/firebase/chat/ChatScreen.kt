package cz.applifting.graphqlempty.firebase.chat

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cz.applifting.graphqlempty.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(navController: NavController) {

    var state by remember { mutableStateOf(ChatState.initial())}
    val viewModel: ChatViewModel = hiltViewModel()

    LaunchedEffect(true) {
        this.launch {
            viewModel.state.collect { state = it }
        }
    }

    LaunchedEffect(state.isUserChecked, state.user) {
        Log.d("Chat", "User checked: ${state.isUserChecked} user: ${state.user}")
        if (state.isUserChecked && state.user == null) {
            navController.navigate(Screen.FirebaseLogin.route) {
                popUpTo(Screen.FirebaseLogin.route) {
                    inclusive = true
                }
            }
        }
    }


    Text("CHat Screen")
}