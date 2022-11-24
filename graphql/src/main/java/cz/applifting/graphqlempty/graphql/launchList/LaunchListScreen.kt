package cz.applifting.graphqlempty.graphql.launchList

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.applifting.graphqlempty.graphql.login.User
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LaunchListScreen(navController: NavController) {

    val viewModel: LaunchListViewModel = koinViewModel()
//    val viewModel: LaunchListViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    var state by remember { mutableStateOf(LaunchListState.initial())}

    val ctx = LocalContext.current

    LaunchedEffect(true) {
        this.launch {
            viewModel.state.collect { state = it}
        }
    }

    if (state.isLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            LaunchListHeader {
                try {
                    User.removeToken(ctx)
                } catch (e: Exception) {
                    Log.e("LLS", "LaunchListScreen: ", e)
                }

            }
        }
        items(state.data) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(72.dp)
                .clickable { scope.launch { navController.navigate("gqlLaunchList/${it.id}") } }, verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = it.mission?.missionPatch, contentDescription = null, modifier = Modifier
                        .size(40.dp)
                        .clip(
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = it.mission?.name ?: "No Data", style = MaterialTheme.typography.body1)
                    Text(text = it.site ?: "No Data", style = MaterialTheme.typography.body2)
                }
            }
        }
    }

    listState.OnBottomReached {
        viewModel.handleAction(LaunchListAction.FetchData)
    }
}

@Composable
fun LazyListState.OnBottomReached(
    loadMore : () -> Unit
){
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    // Convert the state into a cold flow and collect
    LaunchedEffect(shouldLoadMore){
        snapshotFlow { shouldLoadMore.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}

@Composable
fun LaunchListHeader(
    onClick: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 56.dp, vertical = 8.dp)) {
        OutlinedButton(onClick = onClick, modifier = Modifier.width(240.dp)) {
            Text(text = "Log Out")
        }
    }
}