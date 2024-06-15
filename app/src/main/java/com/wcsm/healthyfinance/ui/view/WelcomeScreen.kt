package com.wcsm.healthyfinance.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wcsm.healthyfinance.data.model.MyTopAppBarIcon
import com.wcsm.healthyfinance.data.model.Screen
import com.wcsm.healthyfinance.ui.components.AppTitle
import com.wcsm.healthyfinance.ui.components.MyTopAppBar
import com.wcsm.healthyfinance.ui.components.PrimaryButton
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.Primary

@Composable
fun WelcomeScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        MyTopAppBar(MyTopAppBarIcon.LOGO)

        AppTitle()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(BackgroundContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "BEM VINDO",
                fontSize = 28.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    enabledText = "FAZER LOGIN",
                    onClick = {
                        navController.navigate(Screen.Login.route)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Não tem uma conta?",
                        color = Color.White
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                navController.navigate(Screen.UserRegister.route)
                            },
                        text = "Cadastrar",
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController())
}