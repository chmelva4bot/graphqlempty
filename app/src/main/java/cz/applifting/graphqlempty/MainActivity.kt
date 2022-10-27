package cz.applifting.graphqlempty

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import cz.applifting.graphqlEmpty.LaunchListQuery
import cz.applifting.graphqlempty.navigation.AppNavHost
import cz.applifting.graphqlempty.navigation.Screen
import cz.applifting.graphqlempty.navigation.navDrawer.AppDrawer
import cz.applifting.graphqlempty.navigation.navDrawer.NavDrawerItem
import cz.applifting.graphqlempty.ui.theme.GraphqlEmptyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("192.168.0.100", 9000)
            Firebase.auth.useEmulator("192.168.0.100", 9099)
            Firebase.storage.useEmulator("192.168.0.100", 9199)
        }

        setContent {
            GraphqlEmptyTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val drawerItems by remember(navBackStackEntry) {
        derivedStateOf {
            Screen.getTopLevelScreens()
                .map {
                    NavDrawerItem(it.route, it.title, it.icon, navBackStackEntry?.destination?.route == it.route)
                }
        }
    }
    val title by remember(navBackStackEntry) {
        derivedStateOf {
            Screen.findScreenByRoute(navBackStackEntry?.destination?.route?: "").title
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(title))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { scaffoldState.drawerState.open() }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            },
            drawerContent = {
                AppDrawer(
                    drawerItems = drawerItems,
                    onItemClicked = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }

                        navController.navigate(it.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { inclusive = true}
                        }
                    }
                )
            },
            content = { innerPadding ->
                AppNavHost(
                    navController = navController,
                    snackbarHostState = scaffoldState.snackbarHostState,
                    Modifier.padding(innerPadding)
                )
            }
        )
    }
}