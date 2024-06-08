package com.wcsm.healthyfinance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wcsm.healthyfinance.data.model.BottomNavigationItem
import com.wcsm.healthyfinance.data.model.Screen
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.HealthyFinanceTheme
import com.wcsm.healthyfinance.ui.theme.Primary

val myBottomNavigationItens = listOf(
    BottomNavigationItem(
        title = "Home",
        icon = Icons.Filled.Home,
        screen = Screen.Home
    ),
    BottomNavigationItem(
        title = "Add",
        icon = Icons.Filled.AddCircle,
        screen = Screen.Add
    ),
    BottomNavigationItem(
        title = "Profile",
        icon = Icons.Filled.AccountCircle,
        screen = Screen.Profile
    ),
)

@Composable
fun MyBottomNavigationBar(
    navController: NavHostController,
    popScreen: Boolean = true
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    NavigationBar {
        Row(
            modifier = Modifier.background(BackgroundContainer)
        ) {
            myBottomNavigationItens.forEach { item ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any {
                        it.route == item.screen.route
                    } == true,
                    onClick = {
                        if(currentRoute != item.screen.route) {
                            if(popScreen) navController.popBackStack()
                            navController.navigate(item.screen.route)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = Primary,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = if(item.screen.route == currentRoute) {
                            BackgroundColor
                        } else {
                            Color.Transparent
                        }
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252525)
@Composable
fun AddScreenPreview() {
    HealthyFinanceTheme {
        MyBottomNavigationBar(rememberNavController())
    }
}