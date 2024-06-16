package com.wcsm.healthyfinance.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.wcsm.healthyfinance.data.model.User

interface RegisterRepository {

    suspend fun saveUserFirestore(user: User): Task<Void>

    suspend fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult>

}