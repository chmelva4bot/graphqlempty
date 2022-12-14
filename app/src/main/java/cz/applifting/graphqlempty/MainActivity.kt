package cz.applifting.graphqlempty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.applifting.graphqlempty.navigation.AppNavHost
import cz.applifting.graphqlempty.nav_common.OptionsMenuViewModel
import cz.applifting.graphqlempty.nav_common.Screen
import cz.applifting.graphqlempty.navigation.navDrawer.AppDrawer
import cz.applifting.graphqlempty.navigation.navDrawer.NavDrawerItem
import cz.applifting.graphqlempty.ui.theme.GraphqlEmptyTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (BuildConfig.DEBUG) {
//            val PC_IP = "192.168.1.216"
//            Firebase.database.useEmulator(PC_IP, 9000)
//            Firebase.auth.useEmulator(PC_IP, 9099)
//            Firebase.storage.useEmulator(PC_IP, 9199)
//            Firebase.auth.currentUser.

//        }

//        AuthUI.getInstance().signOut(this)

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

    val optionsMenuViewModel: cz.applifting.graphqlempty.nav_common.OptionsMenuViewModel = koinViewModel()

    val drawerItems by remember(navBackStackEntry) {
        derivedStateOf {
            cz.applifting.graphqlempty.nav_common.Screen.getTopLevelScreens()
                .map {
                    NavDrawerItem(it.route, it.title, it.icon, navBackStackEntry?.destination?.route == it.route)
                }
        }
    }
    val title by remember(navBackStackEntry) {
        derivedStateOf {
            cz.applifting.graphqlempty.nav_common.Screen.findScreenByRoute(navBackStackEntry?.destination?.route?: "").title
        }
    }

    val menu by remember(navBackStackEntry) {
        derivedStateOf {
            cz.applifting.graphqlempty.nav_common.Screen.findScreenByRoute(navBackStackEntry?.destination?.route?: "").optionsMenu
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
                    },
                    actions = { menu(optionsMenuViewModel) }
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
                    optionsMenuViewModel = optionsMenuViewModel,
                    Modifier.padding(innerPadding)
                )
            }
        )
    }
}