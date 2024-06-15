package com.wcsm.healthyfinance.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wcsm.healthyfinance.data.model.LoginFormState
import com.wcsm.healthyfinance.data.repository.LoginRepository
import com.wcsm.healthyfinance.data.repository.LoginRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val TAG = "#FIREBASE_AUTH#"

    //private val loginRepository = LoginRepositoryImpl()

    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState = _loginFormState.asStateFlow()

    fun updateLoginFormState(newState: LoginFormState) {
        _loginFormState.value = newState
    }

    fun rememberPassword() {
        val email = loginFormState.value.email

        val newState = _loginFormState.value.copy(
            emailErrorMessage = null,
            rememberPasswordMessage = null
        )

        if(email.isEmpty()) {
            updateLoginFormState(newState.copy(emailErrorMessage = "Informe o seu e-mail."))
            return
        }

        updateLoginFormState(
            loginFormState.value.copy(
                showRememberPassword = false,
            )
        )
    }

    fun signIn(email: String, password: String) {
        val newState = _loginFormState.value.copy(
            emailErrorMessage = null,
            passwordErrorMessage = null
        )
        updateLoginFormState(newState)

        if(email.isEmpty()) {
            updateLoginFormState(newState.copy(emailErrorMessage = "Informe o seu e-mail."))
            return
        } else if(password.isEmpty()) {
            updateLoginFormState(newState.copy(passwordErrorMessage = "Informe a sua senha."))
            return
        }

        _loginFormState.value = _loginFormState.value.copy(isLoading = true)

        viewModelScope.launch {
            loginRepository.signIn(email, password)
                .addOnSuccessListener {
                    Log.i(TAG, "Usuário LOGADO com SUCESSO!")
                    _loginFormState.value = _loginFormState.value.copy(isLogged = true)
                }
                .addOnFailureListener {
                    Log.i(TAG, "ERRO ao LOGAR USUÁRIO.")
                    val errorMessage = try {
                        throw it
                    } catch (invalidUser: FirebaseAuthInvalidUserException) {
                        invalidUser.printStackTrace()
                        "E-mail não cadastrado."
                    } catch (invalidCredentials: FirebaseAuthInvalidCredentialsException) {
                        invalidCredentials.printStackTrace()
                        "E-mail ou senha estão incorretos."
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                        "Erro inesperado. Tente novamente mais tarde."
                    }

                    updateLoginFormState(
                        newState.copy(
                            emailErrorMessage = if(
                                it is FirebaseAuthInvalidCredentialsException ||
                                it is FirebaseAuthInvalidUserException
                            ) errorMessage else null,
                            isLoading = false
                        )
                    )
                }
        }
    }

    /*fun signIn(email: String, password: String) {
        val newState = _loginFormState.value.copy(
            emailErrorMessage = null,
            passwordErrorMessage = null
        )
        updateLoginFormState(newState)

        if(email.isEmpty()) {
            updateLoginFormState(newState.copy(emailErrorMessage = "Informe o seu e-mail."))
            return
        } else if(password.isEmpty()) {
            updateLoginFormState(newState.copy(passwordErrorMessage = "Informe a sua senha."))
            return
        }

        _loginFormState.value = _loginFormState.value.copy(isLoading = true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.i(TAG, "Usuário LOGADO com SUCESSO!")
                _loginFormState.value = _loginFormState.value.copy(isLogged = true)
            }
            .addOnFailureListener {
                Log.i(TAG, "ERRO ao LOGAR USUÁRIO.")
                val errorMessage = try {
                    throw it
                } catch (invalidUser: FirebaseAuthInvalidUserException) {
                    invalidUser.printStackTrace()
                    "E-mail não cadastrado."
                } catch (invalidCredentials: FirebaseAuthInvalidCredentialsException) {
                    invalidCredentials.printStackTrace()
                    "E-mail ou senha estão incorretos."
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    "Erro inesperado. Tente novamente mais tarde."
                }

                updateLoginFormState(
                    newState.copy(
                        emailErrorMessage = if(
                            it is FirebaseAuthInvalidCredentialsException ||
                            it is FirebaseAuthInvalidUserException
                        ) errorMessage else null,
                        isLoading = false
                    )
                )
            }
    }*/
}