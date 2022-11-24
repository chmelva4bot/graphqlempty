package cz.applifting.graphqlempty.nav_common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cz.applifting.graphqlempty.nav_common.menus.SignOutMenu

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector,
    @StringRes val title: Int,
    val optionsMenu: @Composable (optionsVm: OptionsMenuViewModel) -> Unit = {}
) {

    object GraphQLLaunchList: Screen("gqlLaunchList", R.string.GQLLaunchList, Icons.Default.RocketLaunch, R.string.title_GQLLaunchList)
    object GraphQLLogin: Screen("gqlLogin", R.string.GQLLogin, Icons.Default.RocketLaunch, R.string.title_GQLLogin)
    object GraphQLLaunchDetail: Screen("gqlLaunchList/{id}", R.string.GQLLaunchDetail, Icons.Default.RocketLaunch, R.string.title_GQLLaunchDetail)
    object FirebaseChat: Screen("firebase/chat", R.string.FirebaseChat, Icons.Default.LocalFireDepartment, R.string.title_firebaseChat, { SignOutMenu(it) })
    object FirebaseLogin: Screen("firebase/login", R.string.FirebaseLogin, Icons.Default.LocalFireDepartment, R.string.title_firebaseLogin)

    companion object {
        fun findScreenByRoute(route: String): Screen {
            return when(route) {
                FirebaseChat.route -> FirebaseChat
                FirebaseLogin.route -> FirebaseLogin
                GraphQLLaunchDetail.route -> GraphQLLaunchDetail
                GraphQLLogin.route -> GraphQLLogin
                else -> GraphQLLaunchList
            }
        }

        fun getTopLevelScreens(): List<Screen> = listOf(GraphQLLaunchList, FirebaseChat)
    }
}