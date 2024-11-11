package com.example.wellness.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.wellness.ui.screens.EmptyScreen
import com.example.wellness.ui.screens.HomeScreen
import com.example.wellness.ui.screens.LoginScreen
import com.example.wellness.ui.screens.ProfileScreen
import com.example.wellness.ui.screens.RegisterScreen

@Composable
fun MyHavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = Auth.route,
        modifier = modifier
    ) {
        navigation(
            route = Auth.route,
            startDestination = Login.route
        ) {
            composable(Login.route) {
                LoginScreen (
                    onPerformLogin = { navController.navigateSingleTopWithPopUp(User.route) },
                    onRegisterClick = { navController.navigateSingleTopWithPopUp(Register.route) }
                )
            }
            composable(Register.route) {
                RegisterScreen (
                    onPerformRegister = { navController.navigateSingleTopWithPopUp(Home.route) },
                    onLoginClick = { navController.navigateSingleTopWithPopUp(Login.route) }
                )
            }
        }
        navigation(
            route = User.route,
            startDestination = Home.route
        ) {
            composable(Home.route) { HomeScreen() }
            composable(Profile.route) {
                ProfileScreen { navController.navigateSingleTopWithPopUp(Auth.route) }
            }
            navBarDestinations.drop(2).forEach { dest ->
                composable(dest.route) { EmptyScreen() }
            }
        }
    }
}

fun NavHostController.navigateSingleTopWithPopUp(route: String) {
    val navController = this
    this.navigate(route) {
        this.popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
