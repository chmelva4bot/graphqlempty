package cz.applifting.graphqlempty.navigation

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cz.applifting.graphqlempty.firebase.chat.ui.ChatScreen
import cz.applifting.graphqlempty.firebase.login.FirebaseLoginScreen
import cz.applifting.graphqlempty.graphql.launchDetail.LaunchDetailScreen
import cz.applifting.graphqlempty.graphql.launchList.LaunchListScreen

@Composable
fun AppNavHost(navController: NavHostController, snackbarHostState: SnackbarHostState, optionsMenuViewModel: cz.applifting.graphqlempty.nav_common.OptionsMenuViewModel, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = cz.applifting.graphqlempty.nav_common.Screen.GraphQLLaunchList.route, modifier = modifier) {
        composable(cz.applifting.graphqlempty.nav_common.Screen.GraphQLLaunchList.route) {
            LaunchListScreen(
                navController = navController
            )
        }
        composable(cz.applifting.graphqlempty.nav_common.Screen.GraphQLLaunchDetail.route, arguments = listOf(navArgument("id") { type = NavType.StringType})) {
           LaunchDetailScreen(
                navController = navController,
                snackbarHostState,
                it.arguments?.getString("id") ?: ""
            )
        }
        composable(cz.applifting.graphqlempty.nav_common.Screen.GraphQLLogin.route) { cz.applifting.graphqlempty.graphql.login.LoginScreen(navController = navController) }
        composable(cz.applifting.graphqlempty.nav_common.Screen.FirebaseChat.route) { ChatScreen(navController = navController, optionsMenuViewModel) }
        composable(cz.applifting.graphqlempty.nav_common.Screen.FirebaseLogin.route) { FirebaseLoginScreen(navController = navController) }
    }
}