package com.wcsm.healthyfinance.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface LoginRepository {

    suspend fun signIn(email: String, password: String): Task<AuthResult>

}