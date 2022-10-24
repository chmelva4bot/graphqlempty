package cz.applifting.graphqlempty.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
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
    object GraphQLLaunchDetail: Screen("gqlLaunchList/{id}", R.string.GQLLaunchDetail, Icons.Default.RocketLaunch, R.string.title_GQLLaunchDetail)

    companion object {
        fun findScreenByRoute(route: String): Screen {
            return when(route) {
                else -> GraphQLLaunchList
            }
        }

        fun getTopLevelScreens(): List<Screen> = listOf(GraphQLLaunchList)
    }

}