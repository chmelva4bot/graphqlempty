package cz.applifting.graphqlempty.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.ui.graphics.vector.ImageVector
import cz.applifting.graphqlempty.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector,
    @StringRes val title: Int
) {

    object GraphQLLaunchList: Screen("gqlLaunchList", R.string.GQLLaunchList, Icons.Default.RocketLaunch, R.string.title_GQLLaunchList)
    object GraphQLLogin: Screen("gqlLogin", R.string.GQLLogin, Icons.Default.RocketLaunch, R.string.title_GQLLogin)
    object GraphQLLaunchDetail: Screen("gqlLaunchList/{id}", R.string.GQLLaunchDetail, Icons.Default.RocketLaunch, R.string.title_GQLLaunchDetail)
    object FirebaseChat: Screen("firebase/chat", R.string.FirebaseChat, Icons.Default.LocalFireDepartment, R.string.title_firebaseChat)

    companion object {
        fun findScreenByRoute(route: String): Screen {
            return when(route) {
                FirebaseChat.route -> FirebaseChat
                GraphQLLaunchDetail.route -> GraphQLLaunchDetail
                GraphQLLogin.route -> GraphQLLogin
                else -> GraphQLLaunchList
            }
        }

        fun getTopLevelScreens(): List<Screen> = listOf(GraphQLLaunchList, FirebaseChat)
    }

}