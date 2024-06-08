package com.wcsm.healthyfinance.data.model

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Home : Screen("home")
    object Login : Screen("login")
    object UserRegister : Screen("user_register")
    object Profile : Screen("profile")
    object Detail : Screen("detail")
    object Add : Screen("add")
}