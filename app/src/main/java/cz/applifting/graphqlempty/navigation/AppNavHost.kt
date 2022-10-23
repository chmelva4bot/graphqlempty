package cz.applifting.graphqlempty.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.applifting.graphqlempty.graphql.LaunchListScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.GraphQLLaunchList.route, modifier = modifier) {
        composable(Screen.GraphQLLaunchList.route) { LaunchListScreen(navController = navController)}
    }
}