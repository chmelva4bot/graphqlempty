package cz.applifting.graphqlempty.firebase.chat.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.applifting.graphqlempty.navigation.Screen
import cz.applifting.graphqlempty.ui.theme.LightGray
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(navController: NavController) {

    var state by remember { mutableStateOf(ChatState.initial())}
//    val viewModel: ChatViewModel = hiltViewModel()
    val viewModel: ChatViewModel = koinViewModel()

    val listScrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    val imageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        Log.d("Img", "ChatScreen: ${it.toString()}")
        if (it != null) {
            viewModel.sendAction(ChatAction.SendImageMessage(it))
        }
    }

    var shouldScrollToBottom = remember { true }

    LaunchedEffect(true) {
        this.launch {
            viewModel.state.collect {
                state = it
                if (it.messages.isNotEmpty() && shouldScrollToBottom) {
                    shouldScrollToBottom = false
                    listScrollState.animateScrollToItem(it.messages.lastIndex, 1)
                }
            }
        }
        this.launch {
            viewModel.scrollToBottomEvent.collect {
                scope.launch { listScrollState.animateScrollToItem(state.messages.lastIndex, 1) }
            }
        }
    }

    viewModel.sendAction(ChatAction.CheckUser)

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

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        LazyColumn(
            state = listScrollState,
            //       reverseLayout = true,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {

            items(state.messages) {
                Spacer(modifier = Modifier.height(8.dp))
                MessageBox(
                    name = it.name ?: "",
                    text = it.text ?: "",
                    photoUrl = it.photoUrl ?: "",
                    imageUrl = it.imageUrl ?: "",
                )
            }
//            item {Spacer(modifier = Modifier.height(8.dp))}
        }
        Spacer(modifier = Modifier.height(16.dp))
        ChatScreenBottomBar(
            textFieldValue = state.msgText,
            onSelectImageClicked =  {imageLauncher.launch("image/*")},
            onTextFieldValueChanged = { scope.launch { viewModel.sendAction(ChatAction.UpdateMsgText(it)) }},
            onSendMessageClicked = {scope.launch { viewModel.sendAction(ChatAction.SendMessage) }},
            isSendMessageEnabled = state.msgText.isNotBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ChatScreenBottomBar(
    onSelectImageClicked: ()->Unit,
    textFieldValue: String,
    onTextFieldValueChanged: (String)-> Unit,
    onSendMessageClicked: ()-> Unit,
    isSendMessageEnabled: Boolean
) {
    Row(
        Modifier
            .fillMaxWidth()
        //                .padding(horizontal = 16.dp)
    ) {
        IconButton(onClick = onSelectImageClicked) {
            Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colors.primary)
        }
        Spacer(modifier = Modifier.width(0.dp))
        TextField(
            value = textFieldValue,
            onValueChange = onTextFieldValueChanged,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(0.dp))
        IconButton(onClick = onSendMessageClicked, enabled = isSendMessageEnabled) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = MaterialTheme.colors.primary)
        }
    }
}


@Composable
private fun MessageBox(
    name: String,
    text: String,
    photoUrl: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier, verticalAlignment = Alignment.Bottom) {
        if (photoUrl.isEmpty()) {
            Box(modifier = Modifier
                .background(LightGray.copy(0.3f), CircleShape)
                .size(40.dp))
        } else {
            AsyncImage(model = photoUrl, contentDescription = null, modifier = Modifier
                .size(40.dp)
                .clip(CircleShape))
        }

        Spacer(modifier = Modifier.width(16.dp))
        Column(
            Modifier
                .weight(1f)
                .wrapContentHeight()
                .background(LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.h6)
            if (text.isNotEmpty()) {
                Text(text = text, style = MaterialTheme.typography.body1)
            }
            if (photoUrl.isNotEmpty()) {
                AsyncImage(model = imageUrl, contentDescription = null, contentScale = ContentScale.FillWidth,  modifier = Modifier.fillMaxWidth())
            }
        }
    }
}