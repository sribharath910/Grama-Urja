package com.gramaUrja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gramaUrja.navigation.Routes
import com.gramaUrja.ui.screens.HomeDashboardScreen
import com.gramaUrja.ui.screens.PumpTimerScreen
import com.gramaUrja.ui.screens.SettingsScreen
import com.gramaUrja.ui.screens.SplashScreen
import com.gramaUrja.ui.screens.ZoneSelectionScreen
import com.gramaUrja.ui.theme.GramaUrjaTheme
import com.gramaUrja.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val darkMode by viewModel.darkMode.collectAsState()

            GramaUrjaTheme(darkTheme = darkMode) {
                GramaUrjaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GramaUrjaApp(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigate = { hasZone ->
                    val dest = if (hasZone) Routes.MAIN else Routes.ZONE_SELECTION
                    navController.navigate(dest) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        composable(Routes.ZONE_SELECTION) {
            ZoneSelectionScreen(
                viewModel = viewModel,
                onContinue = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.ZONE_SELECTION) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                viewModel = viewModel,
                onChangeZone = {
                    navController.navigate(Routes.ZONE_SELECTION)
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainScreen(viewModel: MainViewModel, onChangeZone: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", Routes.HOME_DASHBOARD, Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Settings", Routes.SETTINGS, Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                // Pump Timer tab
                val pumpSelected = currentDestination?.hierarchy?.any { it.route == Routes.PUMP_TIMER } == true
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home, // Using Home as placeholder — swap with custom icon
                            contentDescription = "Pump Timer"
                        )
                    },
                    label = { Text("Pump Timer") },
                    selected = pumpSelected,
                    onClick = {
                        navController.navigate(Routes.PUMP_TIMER) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME_DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME_DASHBOARD) {
                HomeDashboardScreen(viewModel = viewModel, onChangeZone = onChangeZone)
            }
            composable(Routes.PUMP_TIMER) {
                PumpTimerScreen(viewModel = viewModel)
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(viewModel = viewModel, onChangeZone = onChangeZone)
            }
        }
    }
}
