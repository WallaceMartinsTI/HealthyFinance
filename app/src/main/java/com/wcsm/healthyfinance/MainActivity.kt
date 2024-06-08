package com.wcsm.healthyfinance

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wcsm.healthyfinance.data.model.Screen
import com.wcsm.healthyfinance.ui.screens.AddBillScreen
import com.wcsm.healthyfinance.ui.screens.DetailScreen
import com.wcsm.healthyfinance.ui.screens.HomeScreen
import com.wcsm.healthyfinance.ui.screens.LoginScreen
import com.wcsm.healthyfinance.ui.screens.ProfileScreen
import com.wcsm.healthyfinance.ui.screens.RegisterScreen
import com.wcsm.healthyfinance.ui.screens.WelcomeScreen
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.HealthyFinanceTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            HealthyFinanceTheme {
                SetBarColor(color = BackgroundColor)

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Welcome.route
                ) {

                    composable(Screen.Welcome.route) {
                        WelcomeScreen(navController = navController)
                    }

                    composable(
                        route = Screen.Login.route,
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(500)
                            )
                        }
                    ) {
                        LoginScreen(navController = navController) { exitApp() }
                    }

                    composable(Screen.UserRegister.route) {
                        RegisterScreen(navController = navController)
                    }

                    composable(
                        route = Screen.Home.route + "?userDeleted={userDeleted}",
                        arguments = listOf(
                            navArgument("userDeleted") {
                                type = NavType.StringType
                                defaultValue = "false"
                                nullable = true
                            }
                        )
                    ) {
                        HomeScreen(
                            navController = navController,
                            backStackEntry = it
                        ) { exitApp() }
                    }

                    composable(Screen.Profile.route) {
                        ProfileScreen(navController = navController)
                    }

                    composable(
                        route = Screen.Detail.route + "/{billType}",
                        arguments = listOf(
                            navArgument("billType") {type = NavType.StringType}
                        ),
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(500)
                            )
                        }
                    ) {
                        DetailScreen(
                            navController = navController,
                            backStackEntry = it
                        )
                    }

                    composable(Screen.Add.route) {
                        AddBillScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(color = color)
        }
    }

    private fun exitApp() {
        finish()
    }
}