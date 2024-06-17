package com.wcsm.healthyfinance.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Transaction
import com.wcsm.healthyfinance.data.model.AddBillFormState

interface BillRepository {

    suspend fun saveBillFirestore(
        currentUser: FirebaseUser,
        addBillFormState: AddBillFormState,
        filteredValue: Double,
        formattedDate: Timestamp
    ): Task<Transaction>

    suspend fun deleteBillFirestore(
        currentUser: FirebaseUser,
        billId: String
    ): Task<Unit>

}