package cz.applifting.graphqlempty.navigation

import androidx.annotation.StringRes
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import cz.applifting.graphqlempty.R
import cz.applifting.graphqlempty.firebase.chat.FirebaseMenu
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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
    object FirebaseChat: Screen("firebase/chat", R.string.FirebaseChat, Icons.Default.LocalFireDepartment, R.string.title_firebaseChat, { FirebaseMenu(it)})
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