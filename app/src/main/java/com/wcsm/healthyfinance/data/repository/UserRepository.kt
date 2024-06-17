package com.wcsm.healthyfinance.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.wcsm.healthyfinance.data.model.User

interface UserRepository {

    suspend fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult>

    suspend fun saveUserFirestore(user: User): Task<Void>

    suspend fun signIn(email: String, password: String): Task<AuthResult>

    suspend fun fetchUserData(user: FirebaseUser): Task<DocumentSnapshot>

    suspend fun updateUserFirebase(
        user: FirebaseUser,
        newName: String,
        newBirthDate: Timestamp,
        newGender: String
    ): Task<Void>

    suspend fun deleteUserFirebaseAuth(user: FirebaseUser): Task<Unit>

    suspend fun deleteUserFirestore(user: FirebaseUser): Task<Void>

}