package cz.applifting.graphqlempty.graphql.launchDetail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LaunchDetailScreen(navController: NavController, id: String) {
    Text(text = "Launch with Id: $id")
}