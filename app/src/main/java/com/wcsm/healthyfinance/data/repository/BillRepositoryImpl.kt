package com.wcsm.healthyfinance.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.wcsm.healthyfinance.data.model.AddBillFormState
import com.wcsm.healthyfinance.data.model.Bill
import com.wcsm.healthyfinance.data.model.BillCategory
import com.wcsm.healthyfinance.data.model.User
import java.util.UUID
import javax.inject.Inject

class BillRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BillRepository {

    override suspend fun saveBillFirestore(
        currentUser: FirebaseUser,
        addBillFormState: AddBillFormState,
        filteredValue: Double,
        formattedDate: Timestamp
    ): Task<Transaction> {

        val userDocRef = firestore.collection("users").document(currentUser.uid)

        val newBill = Bill(
            userId = currentUser.uid,
            id = UUID.randomUUID().toString().replace("-", ""),
            description = addBillFormState.description,
            billCategory = BillCategory(
                type = addBillFormState.type,
                name = addBillFormState.category
            ),
            value = filteredValue,
            installment = addBillFormState.installment,
            date = formattedDate,
        )

        return firestore.runTransaction { transaction ->
            val userDoc = transaction.get(userDocRef)
            val userBills = userDoc.get("bills") as? MutableList<HashMap<String, Any>> ?: mutableListOf()

            val newBillMap = hashMapOf(
                "id" to newBill.id,
                "userId" to newBill.userId,
                "description" to newBill.description,
                "billCategory" to hashMapOf(
                    "type" to newBill.billCategory.type,
                    "name" to newBill.billCategory.name
                ),
                "value" to newBill.value,
                "installment" to newBill.installment,
                "date" to newBill.date
            )

            userBills.add(newBillMap)

            transaction.update(userDocRef, "bills", userBills)
        }
    }

    override suspend fun deleteBillFirestore(
        currentUser: FirebaseUser,
        billId: String
    ): Task<Unit> {
        val userDocRef = firestore.collection("users").document(currentUser.uid)

        return firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)
            val user = snapshot.toObject(User::class.java)

            if(user != null) {
                val updatedBills = user.bills.filterNot { it.id == billId }

                transaction.update(userDocRef, "bills", updatedBills)
            }
        }
    }

}