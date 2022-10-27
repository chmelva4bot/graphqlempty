package cz.applifting.graphqlempty.navigation

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cz.applifting.graphqlempty.firebase.chat.ChatScreen
import cz.applifting.graphqlempty.firebase.login.FirebaseLoginScreen
import cz.applifting.graphqlempty.graphql.launchList.LaunchListScreen
import cz.applifting.graphqlempty.graphql.launchDetail.LaunchDetailScreen
import cz.applifting.graphqlempty.graphql.login.LoginScreen

@Composable
fun AppNavHost(navController: NavHostController, snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.GraphQLLaunchList.route, modifier = modifier) {
        composable(Screen.GraphQLLaunchList.route) { LaunchListScreen(navController = navController) }
        composable(Screen.GraphQLLaunchDetail.route, arguments = listOf(navArgument("id") { type = NavType.StringType})) {
            LaunchDetailScreen(navController = navController, snackbarHostState, it.arguments?.getString("id") ?: "")
        }
        composable(Screen.GraphQLLogin.route) { LoginScreen(navController = navController) }
        composable(Screen.FirebaseChat.route) { ChatScreen(navController = navController)}
        composable(Screen.FirebaseLogin.route) { FirebaseLoginScreen(navController = navController) }
    }
}