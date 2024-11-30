package com.example.proyectofinaltarpuy.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaltarpuy.ui.theme.screens.ForgotPasswordScreen
import com.example.proyectofinaltarpuy.ui.theme.screens.InicioScreen
import com.example.proyectofinaltarpuy.ui.theme.screens.LoginScreen
import com.example.proyectofinaltarpuy.ui.theme.screens.MapScreen
import com.example.proyectofinaltarpuy.ui.theme.screens.RegisterScreen
import com.example.proyectofinaltarpuy.ui.theme.screens.SplashScreen
import com.example.proyectofinaltarpuy.viewmodels.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyectofinaltarpuy.ui.theme.screens.WeatherScreen // Importa la función WeatherApp
import com.example.proyectofinaltarpuy.viewmodels.MapViewModel
import com.example.proyectofinaltarpuy.viewmodels.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToInicio = { navController.navigate("inicio") }
            )
        }
        composable("inicio") {
            InicioScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { username ->
                    navController.navigate("main/$username") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
                }
            )
        }
        composable("main/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            val weatherViewModel: WeatherViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                if (weatherViewModel.currentWeather == null) {
                    weatherViewModel.fetchWeatherData("Arequipa", "e3c54ae44ea4052e60a4fa2b30ffbe8f")
                }
            }

            WeatherScreen(viewModel = weatherViewModel)
        }
        composable("forgot_password") {
            ForgotPasswordScreen()
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("map") {
            val mapViewModel: MapViewModel = hiltViewModel()
            MapScreen(viewModel = mapViewModel)
        }
    }
}


