package com.vigilante.codesnippetquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vigilante.codesnippetquest.ui.ViewModelFactory
import com.vigilante.codesnippetquest.ui.auth.LoginScreen
import com.vigilante.codesnippetquest.ui.auth.RegisterScreen
import com.vigilante.codesnippetquest.ui.game.GameplayScreen
import com.vigilante.codesnippetquest.ui.history.HistoryScreen
import com.vigilante.codesnippetquest.ui.home.HomeScreen
import com.vigilante.codesnippetquest.ui.settings.SettingsScreen
import com.vigilante.codesnippetquest.ui.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home/{userId}") {
        fun createRoute(userId: Int) = "home/$userId"
    }
    object Gameplay : Screen("gameplay/{userId}/{level}") {
        fun createRoute(userId: Int, level: Int) = "gameplay/$userId/$level"
    }
    object History : Screen("history/{userId}") {
        fun createRoute(userId: Int) = "history/$userId"
    }
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelFactory,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToLogin = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel(factory = viewModelFactory),
                onLoginSuccess = { userId ->
                    navController.navigate(Screen.Home.createRoute(userId)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = viewModel(factory = viewModelFactory),
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Home.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: -1
            HomeScreen(
                userId = userId,
                viewModel = viewModel(factory = viewModelFactory),
                onNavigateToGameplay = { level ->
                    navController.navigate(Screen.Gameplay.createRoute(userId, level))
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.createRoute(userId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Gameplay.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: -1
            val level = backStackEntry.arguments?.getString("level")?.toIntOrNull() ?: 1
            GameplayScreen(
                userId = userId,
                level = level,
                viewModel = viewModel(factory = viewModelFactory),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.History.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: -1
            HistoryScreen(
                userId = userId,
                viewModel = viewModel(factory = viewModelFactory),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Settings.route) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val prefs = context.getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)
            val userId = prefs.getInt("userId", -1)
            SettingsScreen(
                userId = userId,
                viewModel = viewModel(factory = viewModelFactory),
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onLanguageChanged = {
                    // Language is now handled reactively via AppSettings.locale
                    // No activity restart needed
                }
            )
        }
    }
}
