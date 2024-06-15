package org.d3if3132.assesment03.memories.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavGraph(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomNavGraph(
        route = Route.HOME_SCREEN,
        title = "Home",
        icon = Icons.Default.Home
    )
    data object Profile : BottomNavGraph(
        route = Route.PROFILE_SCREEN,
        title = "Profile",
        icon = Icons.Default.AccountBox
    )
}