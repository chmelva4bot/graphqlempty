package cz.applifting.graphqlempty.firebase.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.applifting.graphqlempty.navigation.Screen
import cz.applifting.graphqlempty.ui.theme.LightGray
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(navController: NavController) {

    var state by remember { mutableStateOf(ChatState.initial())}
//    val viewModel: ChatViewModel = hiltViewModel()
    val viewModel: ChatViewModel = koinViewModel()

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


   LazyColumn(
       reverseLayout = true,
       modifier = Modifier.padding(horizontal = 16.dp),
       verticalArrangement = Arrangement.spacedBy(8.dp),
       contentPadding = PaddingValues(vertical = 8.dp)
   ) {

       items(state.messages) {
           MessageBox(
               name = it.name ?: "",
               text = it.text ?: "",
               photoUrl = it.photoUrl ?: "",
               imageUrl = it.imageUrl ?: "",
           )
       }
   }
}


@Composable
fun MessageBox(
    name: String,
    text: String,
    photoUrl: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier, verticalAlignment = Alignment.Bottom) {
        Box(modifier = Modifier
            .background(LightGray.copy(0.3f), CircleShape)
            .size(40.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            Modifier
                .weight(1f)
                .wrapContentHeight()
                .background(LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.h6)
            Text(text = text, style = MaterialTheme.typography.body1)
        }
    }
}