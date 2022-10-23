package cz.applifting.graphqlempty.navigation.navDrawer

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavDrawerItem(
    val route: String,
    @StringRes val title:  Int,
    val icon: ImageVector,
    val isSelected: Boolean
)
