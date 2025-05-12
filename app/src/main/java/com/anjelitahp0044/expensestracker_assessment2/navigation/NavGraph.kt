package com.anjelitahp0044.expensestracker_assessment2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anjelitahp0044.expensestracker_assessment2.screen.AboutScreen
import com.anjelitahp0044.expensestracker_assessment2.screen.DetailScreen
import com.anjelitahp0044.expensestracker_assessment2.screen.KEY_ID_PENGELUARAN
import com.anjelitahp0044.expensestracker_assessment2.screen.MainScreen
import com.anjelitahp0044.expensestracker_assessment2.screen.RecycleBinScreen


@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController(), isDarkTheme: MutableState<Boolean>){
    NavHost(
        navController=navController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route){
            MainScreen(navController = navController, isDarkTheme = isDarkTheme)
        }
        composable(route = Screen.FormBaru.route) {
            DetailScreen(navController = navController, isDarkTheme = isDarkTheme)
        }
        composable(route = Screen.RecycleBin.route) {
            RecycleBinScreen(navController, isDarkTheme = isDarkTheme)
        }

        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_PENGELUARAN) { type = NavType.LongType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_PENGELUARAN)
            DetailScreen(navController = navController, id = id, isDarkTheme = isDarkTheme)
        }
        composable(route = Screen.About.route) {
            AboutScreen(
                navController = navController,
                isDarkTheme = isDarkTheme
            )
        }
    }
}
