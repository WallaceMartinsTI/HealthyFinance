package com.wcsm.healthyfinance.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wcsm.healthyfinance.data.model.Screen
import com.wcsm.healthyfinance.data.model.User
import com.wcsm.healthyfinance.ui.util.parseDateFromString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val TAG = "#FIREBASE_AUTH#"

    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoadingUpdate = MutableStateFlow(false)
    val isLoadingUpdate = _isLoadingUpdate.asStateFlow()

    private val _updateMessage = MutableStateFlow("")
    val updateMessage = _updateMessage.asStateFlow()

    fun setLoading(status: Boolean) {
        _isLoading.value = status
    }

    fun setLoadingUpdate(status: Boolean) {
        _isLoadingUpdate.value = status
    }

    fun setUpdateMessage(message: String) {
        _updateMessage.value = message
    }


    init {
        fetchUserData()
    }

    fun updateUserProfile(name: String, birthDate: String, gender: String) {
        setUpdateMessage("")

        val userId = auth.currentUser?.uid
        if(userId != null) {
            val formattedBirthDate = parseDateFromString(birthDate)
            if(formattedBirthDate != null) {
                updateUserFirebaseProfile(
                    userId,
                    name,
                    formattedBirthDate,
                    gender
                )
            }
        }
    }

    fun deleteUserFirestore(navController: NavHostController) {
        val userId = auth.currentUser?.uid

        if(userId != null) {
            val userDocRef = firestore.collection("users").document(userId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)

                if(snapshot.exists()) {
                    transaction.delete(userDocRef)
                }
            }.addOnSuccessListener {
                auth.currentUser?.delete()?.addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Log.i(TAG, "SUCESSO ao deletar usuário.")
                        navController.navigate(Screen.Home.route + "?userDeleted=true")
                    } else {
                        Log.i(TAG, "ERRO ao deletar usuário autenticado.")
                    }
                }
            }.addOnFailureListener {
                Log.i(TAG, "ERRO ao deletar usuário no firestore.")
            }
        }
    }

    private fun updateUserFirebaseProfile(userId: String, newName: String, newBirthDate: Timestamp, newGender: String) {
        firestore
            .collection("users")
            .document(userId)
            .update(
                mapOf(
                    "name" to newName,
                    "birthDate" to newBirthDate,
                    "gender" to newGender
                )
            )
            .addOnSuccessListener {
                Log.i(TAG, "SUCESSO ao Atualizar Perfil")
                setUpdateMessage("Perfil Atualizado com Sucesso!")
            }
            .addOnFailureListener {
                Log.i(TAG, "ERRO ao Atualizar Perfil")
                setUpdateMessage("Erro ao Atualizar Perfil.")
            }

        setLoadingUpdate(false)
    }

    private fun fetchUserData() {
        val userId = auth.currentUser?.uid
        if(userId != null) {
            firestore
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener {document ->
                    val user = document.toObject(User::class.java)
                    _userData.value = user
                    Log.i(TAG, "Busca de Usuário no FIRESTORE com SUCESSO!")
                    setLoading(false)
                }
                .addOnFailureListener {
                    Log.i(TAG, "ERRO ao buscar USUÁRIO no FIRESTORE.")
                }
        }
    }
}