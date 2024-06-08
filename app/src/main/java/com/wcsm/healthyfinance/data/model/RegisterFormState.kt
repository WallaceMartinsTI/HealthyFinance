package com.wcsm.healthyfinance.data.model

data class RegisterFormState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val confirmPasswordErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false
)
