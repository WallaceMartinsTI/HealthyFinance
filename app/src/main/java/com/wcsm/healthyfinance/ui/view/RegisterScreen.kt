package com.wcsm.healthyfinance.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.wcsm.healthyfinance.ui.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val registerFormState by registerViewModel.registerFormState.collectAsState()

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
            RegisterTextFieldWithError(
                modifier = Modifier.width(280.dp),
                fieldName = "Email",
                label = "Email",
                value = registerFormState.email,
                onValueChange = { email ->
                    if(registerFormState.email.length < 50) {
                        registerViewModel.updateRegisterFormState(registerFormState.copy(email = email))
                    }
                },
                errorMessage = registerFormState.emailErrorMessage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                unfocusedLabelColorExpression = registerFormState.email.isNotEmpty(),
                trailingIcon = {
                    if(registerFormState.email.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear email icon",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    registerViewModel.updateRegisterFormState(
                                        registerFormState.copy(
                                            email = ""
                                        )
                                    )
                                }
                        )
                    }
                }
            )

            RegisterTextFieldWithError(
                fieldName = "Senha",
                label = "Senha",
                value = registerFormState.password,
                onValueChange = { password ->
                    registerViewModel.updateRegisterFormState(registerFormState.copy(password = password))
                },
                errorMessage = registerFormState.passwordErrorMessage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (registerFormState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                unfocusedLabelColorExpression = registerFormState.password.isNotEmpty(),
                trailingIcon = {
                    Icon(
                        imageVector = if (registerFormState.showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                registerViewModel.updateRegisterFormState(
                                    registerFormState.copy(
                                        showPassword = !registerFormState.showPassword
                                    )
                                )
                            }
                    )
                }
            )

            RegisterTextFieldWithError(
                fieldName = "Confirme sua senha",
                label = "Confirme sua senha",
                value = registerFormState.confirmPassword,
                onValueChange = { confirmPassword ->
                    registerViewModel.updateRegisterFormState(registerFormState.copy(confirmPassword = confirmPassword))
                },
                errorMessage = registerFormState.confirmPasswordErrorMessage,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (registerFormState.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                unfocusedLabelColorExpression = registerFormState.confirmPassword.isNotEmpty(),
                trailingIcon = {
                    Icon(
                        imageVector = if (registerFormState.showConfirmPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                registerViewModel.updateRegisterFormState(
                                    registerFormState.copy(
                                        showConfirmPassword = !registerFormState.showConfirmPassword
                                    )
                                )
                            }
                    )
                }
            )

            Spacer(modifier = Modifier.weight(0.5f))

            if (registerFormState.isLoading) {
                CircularLoading(modifier = Modifier.width(280.dp), size = 50.dp)
            }

            Spacer(modifier = Modifier.weight(1f))

            LaunchedEffect(registerFormState.isRegistered) {
                if (registerFormState.isRegistered) {
                    navController.navigate(Screen.Login.route)
                }
            }

            PrimaryButton(
                modifier = Modifier.width(280.dp),
                enabled = !registerFormState.isLoading,
                enabledText = "CADASTRAR",
                disabledText = "CARREGANDO...",
                onClick = {
                    registerViewModel.createUser()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RegisterTextFieldWithError(
    modifier: Modifier = Modifier,
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
    Column {
        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            text = fieldName,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )

        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = BackgroundColor,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor =
                if(unfocusedLabelColorExpression) Primary else Color.Gray.copy(alpha = 0.8f),
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
}