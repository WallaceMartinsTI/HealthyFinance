package com.wcsm.healthyfinance.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.wcsm.healthyfinance.data.model.RegisterFormState
import com.wcsm.healthyfinance.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class RegisterViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val TAG = "#FIREBASE_AUTH#"

    private val _registerFormState = MutableStateFlow(RegisterFormState())
    val registerFormState = _registerFormState.asStateFlow()

    fun updateRegisterFormState(newState: RegisterFormState) {
        _registerFormState.value = newState
    }

    fun createUser() {
        val email = registerFormState.value.email
        val password = registerFormState.value.password
        val confirmPassword = registerFormState.value.confirmPassword

        val newState = _registerFormState.value.copy(
            emailErrorMessage = null,
            passwordErrorMessage = null,
            confirmPasswordErrorMessage = null
        )
        updateRegisterFormState(newState)

        if (email.isEmpty()) {
            updateRegisterFormState(newState.copy(emailErrorMessage = "Informe um e-mail."))
            return
        } else if(email.length > 50) {
            updateRegisterFormState(newState.copy(emailErrorMessage = "Tamanho máximo de 50 caracteres."))
            return
        } else if(password.isEmpty()) {
            updateRegisterFormState(newState.copy(passwordErrorMessage = "Informe uma senha."))
            return
        } else if(password.length > 50) {
            updateRegisterFormState(newState.copy(passwordErrorMessage = "Tamanho máximo de 50 caracteres."))
            return
        } else if (confirmPassword.isEmpty()) {
            updateRegisterFormState(newState.copy(confirmPasswordErrorMessage = "Repita a sua senha."))
            return
        } else if (password != confirmPassword) {
            updateRegisterFormState(
                newState.copy(
                    passwordErrorMessage = "As senhas estão diferentes.",
                    confirmPasswordErrorMessage = "As senhas estão diferentes."
                )
            )
            return
        }

        _registerFormState.value = _registerFormState.value.copy(isLoading = true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = it.user?.uid
                if (userId != null) {
                    val defaultRandomName = getRandomName()
                    val newUser = User(
                        id = userId,
                        name = defaultRandomName,
                        email = email,
                        gender = "Selecione o seu gênero",
                        bills = emptyList()
                    )
                    saveUserFirestore(newUser)
                }
            }
            .addOnFailureListener {
                Log.i(TAG, "ERRO ao CRIAR USUÁRIO.")
                var emailErrorMessage = ""
                var passwordErrorMessage = ""
                var confirmPasswordErrorMessage = ""

                try {
                    throw it
                } catch (weakPassword: FirebaseAuthWeakPasswordException) {
                    weakPassword.printStackTrace()
                    passwordErrorMessage = "Senha fraca, tente outra."
                    confirmPasswordErrorMessage = "Senha fraca, tente outra."
                } catch (userAlreadyExist: FirebaseAuthUserCollisionException) {
                    userAlreadyExist.printStackTrace()
                    emailErrorMessage = "E-mail já cadastrado."
                } catch (invalidCredentials: FirebaseAuthInvalidCredentialsException) {
                    emailErrorMessage = "E-mail inválido, tente novamente."
                } catch (e: Exception) {
                    confirmPasswordErrorMessage = "Erro inesperado. Tente novamente mais tarde."
                }

                updateRegisterFormState(
                    newState.copy(
                        emailErrorMessage = emailErrorMessage.ifEmpty { null },
                        passwordErrorMessage = passwordErrorMessage.ifEmpty { null },
                        confirmPasswordErrorMessage = confirmPasswordErrorMessage.ifEmpty { null },
                        isLoading = false
                    )
                )
            }
    }

    private fun saveUserFirestore(user: User) {
        firestore
            .collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                Log.i(TAG, "Usuário SALVO no FIRESTORE com SUCESSO!")
                Log.i(TAG, "Usuário CRIADO com SUCESSO!")
                _registerFormState.value = _registerFormState.value.copy(isRegistered = true)
            }
            .addOnFailureListener {
                Log.i(TAG, "ERRO ao SALVAR USUÁRIO no FIRESTORE.")
                try {
                    throw it
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

    private fun getRandomName(): String {
        val random = Random(System.currentTimeMillis())
        return "user${(0 until 5).joinToString("") { random.nextInt(0, 10).toString() }}"
    }
}