package com.wcsm.healthyfinance.ui.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.wcsm.healthyfinance.data.model.Screen
import com.wcsm.healthyfinance.ui.components.AppTitle
import com.wcsm.healthyfinance.ui.components.CircularLoading
import com.wcsm.healthyfinance.ui.components.ErrorContainer
import com.wcsm.healthyfinance.ui.components.MyTopAppBar
import com.wcsm.healthyfinance.ui.components.PrimaryButton
import com.wcsm.healthyfinance.ui.theme.BackgroundColor
import com.wcsm.healthyfinance.ui.theme.BackgroundContainer
import com.wcsm.healthyfinance.ui.theme.Primary
import com.wcsm.healthyfinance.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    exitApp: () -> Unit = {}
) {
    val loginFormState by loginViewModel.loginFormState.collectAsState()
    val isConnected by loginViewModel.isConnected.collectAsState()

    var showFinishAppDialog by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }

    BackHandler {
        showFinishAppDialog = true
    }

    if(showFinishAppDialog) {
        AlertDialog(
            onDismissRequest = { showFinishAppDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout icon"
                )
            },
            title = { Text("Sair do Aplicativo") },
            text = {
                Text(
                    text = "Deseja realmente sair do aplicativo?",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabledText = "Sair do App"
                ) {
                    showFinishAppDialog = false
                    exitApp()
                }
            },
            containerColor = BackgroundColor,
            iconContentColor = Primary,
            titleContentColor = Primary,
            textContentColor = Color.White
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        MyTopAppBar()

        AppTitle()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(BackgroundContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.width(280.dp)) {
                LoginTextFieldWithError(
                    fieldName = "Email",
                    label = "Email",
                    value = loginFormState.email,
                    onValueChange = { email ->
                        loginViewModel.updateLoginFormState(loginFormState.copy(email = email))
                    },
                    errorMessage = loginFormState.emailErrorMessage,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    unfocusedLabelColorExpression = loginFormState.email.isNotEmpty(),
                    trailingIcon = {
                        if (loginFormState.email.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear icon",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable {
                                        loginViewModel.updateLoginFormState(
                                            loginFormState.copy(
                                                email = ""
                                            )
                                        )
                                    }
                            )
                        }
                    }
                )

                LoginTextFieldWithError(
                    fieldName = "Senha",
                    label = "Senha",
                    value = loginFormState.password,
                    onValueChange = { password ->
                        loginViewModel.updateLoginFormState(
                            loginFormState.copy(
                                password = password
                            )
                        )
                    },
                    errorMessage = loginFormState.passwordErrorMessage,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    unfocusedLabelColorExpression = loginFormState.password.isNotEmpty(),
                    trailingIcon = {
                        Icon(
                            imageVector =
                            if (loginFormState.showPassword) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    loginViewModel.updateLoginFormState(
                                        loginFormState.copy(
                                            showPassword = !loginFormState.showPassword
                                        )
                                    )
                                }
                        )
                    },
                    visualTransformation =
                    if (loginFormState.showPassword) VisualTransformation.None
                    else PasswordVisualTransformation()
                )

                if (!loginFormState.isLoading) {
                    Text(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .clickable {
                                if (loginFormState.showRememberPassword) {
                                    loginViewModel.forgotPassword()
                                }
                            },
                        text = if(loginFormState.showRememberPassword) "Esqueci minha senha"
                        else "E-mail enviado.",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (loginFormState.isLoading) {
                    CircularLoading(
                        modifier = Modifier.fillMaxWidth(),
                        size = 80.dp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                LaunchedEffect(loginFormState.isLogged) {
                    if (loginFormState.isLogged) {
                        loginViewModel.updateLoginFormState(loginFormState.copy(isLoading = false))
                        navController.navigate(Screen.Home.route)
                    }
                }

                if(errorMessage.isNotEmpty()) {
                    ErrorContainer(
                        errorMessage = errorMessage,
                        errorIcon = {
                            Icon(
                                imageVector = Icons.Filled.WifiOff,
                                contentDescription = "Wifi Off Icon",
                                tint = Color.White
                            )
                        }
                    )
                }

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    enabledText = "ENTRAR",
                    disabledText = "CARREGANDO...",
                    enabled = !loginFormState.isLoading,
                    onClick = {
                        errorMessage = ""

                        loginViewModel.checkConnection()
                        if(isConnected) {
                            loginViewModel.signIn(loginFormState.email, loginFormState.password)
                        } else {
                            errorMessage = "Sem conexão no momento, tente mais tarde."
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun LoginTextFieldWithError(
    fieldName: String,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    unfocusedLabelColorExpression: Boolean,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Text(
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
        text = fieldName,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.White
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        textStyle = TextStyle(color = Color.White),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = BackgroundColor,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor =
            if (unfocusedLabelColorExpression) Primary else Color.Gray.copy(alpha = 0.8f),
            focusedBorderColor = Color.White,
            cursorColor = Primary,
            unfocusedBorderColor = Color.White,
            selectionColors = TextSelectionColors(
                Primary, Color.Transparent
            )
        ),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
    errorMessage?.let {
        ErrorContainer(errorMessage = it)
    }
}