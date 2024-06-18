package com.wcsm.healthyfinance.data.model

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val rememberPasswordMessage: String? = null,
    val isLoading: Boolean = false,
    val isLogged: Boolean = false,
    val showPassword: Boolean = false,
    val showRememberPassword: Boolean = true
)
