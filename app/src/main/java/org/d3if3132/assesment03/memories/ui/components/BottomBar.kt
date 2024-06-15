package org.d3if3132.assesment03.memories.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.d3if3132.assesment03.memories.navigation.BottomNavGraph

@Composable
fun BottomBar(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val screens = listOf(
        BottomNavGraph.Home,
        BottomNavGraph.Profile
    )
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any{
        it.route == currentDestination?.route
    }

    if (bottomBarDestination){
        NavigationBar(containerColor = MaterialTheme.colorScheme.primary, modifier = modifier){
            screens.forEach{screen ->
               NavigationBarItem(screen = screen, currentDestination = currentDestination, navController = navHostController)
            }
        }
    }
}

@Composable
fun RowScope.NavigationBarItem(screen: BottomNavGraph, currentDestination: NavDestination?, navController: NavHostController, hierarchy: Boolean = currentDestination?.hierarchy?.any{
    it.route == screen.route
} == true) {
    NavigationBarItem(
        selected = hierarchy,
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        label = {
            Text(text = screen.title, color = if (hierarchy) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface)
        },
        icon = {
            Icon(
                imageVector = screen.icon, contentDescription = "Navigation Icon"
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.surface,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.surface
        ),
        )
}