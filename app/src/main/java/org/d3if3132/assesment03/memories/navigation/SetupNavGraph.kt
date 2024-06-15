package org.d3if3132.assesment03.memories.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.d3if3132.assesment03.memories.model.User
import org.d3if3132.assesment03.memories.ui.presentation.MainViewModel
import org.d3if3132.assesment03.memories.ui.screen.HomeScreen
import org.d3if3132.assesment03.memories.ui.screen.ProfileScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    user: User,
    viewModel: MainViewModel
) {
    NavHost(navController = navController, startDestination = Route.MAIN, route = Route.ROOT){
        navigation(startDestination = BottomNavGraph.Home.route, route = Route.MAIN){
            composable(route = BottomNavGraph.Home.route){
                HomeScreen(navController = navController)
            }
            composable(route = BottomNavGraph.Profile.route){
                ProfileScreen(navController = navController)
            }
        }
    }
}