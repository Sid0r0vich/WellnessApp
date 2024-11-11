package com.example.wellness

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wellness.ui.components.TopAppBar
import com.example.wellness.ui.navigation.BottomNavigationBar
import com.example.wellness.ui.navigation.Home
import com.example.wellness.ui.navigation.Login
import com.example.wellness.ui.navigation.MyHavHost
import com.example.wellness.ui.navigation.NavDestination
import com.example.wellness.ui.navigation.navBarDestinations
import com.example.wellness.ui.navigation.navDestinations
import com.example.wellness.ui.theme.WellnessAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WellnessAppTheme {
                WellnessApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WellnessApp() {
    WellnessAppTheme {
        val registered = false
        val navController = rememberNavController()
        navController.addOnDestinationChangedListener { controller, _, _ ->
            val routes = controller
                .currentBackStack.value
                .map { it.destination.route }
                .joinToString(", ")

            Log.d("BackStackLog", "BackStack: $routes")
        }
        val currentScreen: NavDestination = navDestinations.find {
            val currentDestination = navController
                .currentBackStackEntryAsState()
                .value
                ?.destination
            it.route == currentDestination?.route
        } ?: if (registered) Home else Login
        val bottomBarVisibility = currentScreen in navBarDestinations

        Scaffold(
            topBar = { TopAppBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                currentScreen = currentScreen,
                ) },
            bottomBar = {
                if (bottomBarVisibility) BottomNavigationBar(
                    currentScreen = currentScreen
                ) { route ->
                    navController.navigate(route) {
                        popUpTo(Home.route)
                        launchSingleTop = true
                    }
                }
            }
        ) { innerPadding ->
            MyHavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}