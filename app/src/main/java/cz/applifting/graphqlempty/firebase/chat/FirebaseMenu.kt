package cz.applifting.graphqlempty.firebase.chat

import android.util.Log
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import cz.applifting.graphqlempty.navigation.OptionMenuItems
import cz.applifting.graphqlempty.navigation.OptionsMenuViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FirebaseMenu(optionsMenuViewModel: OptionsMenuViewModel) {

    var showMenu by remember { mutableStateOf(false) }
    val scope  = rememberCoroutineScope()

    Log.d("aaa", optionsMenuViewModel.toString())

    IconButton(onClick = { showMenu = !showMenu }) {
        Icon(Icons.Default.MoreVert, null, tint = MaterialTheme.colors.onPrimary)
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        DropdownMenuItem(onClick = {
            scope.launch {
                optionsMenuViewModel.onOptionsItemSelected(OptionMenuItems.SignOut)
                showMenu = false
            }
        }) {
            Text(text = "Sign Out")
        }
    }
}