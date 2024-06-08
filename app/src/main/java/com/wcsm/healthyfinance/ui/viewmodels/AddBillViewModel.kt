package com.wcsm.healthyfinance.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wcsm.healthyfinance.data.model.AddBillFormState
import com.wcsm.healthyfinance.data.model.Bill
import com.wcsm.healthyfinance.data.model.BillCategory
import com.wcsm.healthyfinance.ui.util.parseDateFromString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AddBillViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val TAG = "#FIREBASE_AUTH#"

    private val _addBillFormState = MutableStateFlow(AddBillFormState())
    val addBillFormState = _addBillFormState.asStateFlow()

    private val _updateMessage = MutableStateFlow("")
    val updateMessage = _updateMessage.asStateFlow()

    private val _billAdded = MutableStateFlow(false)
    val billAdded = _billAdded.asStateFlow()

    fun updateAddBillFormState(newState: AddBillFormState) {
        _addBillFormState.value = newState
    }

    fun setBillAdded(status: Boolean) {
        _billAdded.value = status
    }

    fun saveBill(date: String) {
        val value = addBillFormState.value.value
        val description = addBillFormState.value.description

        val newState = _addBillFormState.value.copy(
            valueErrorMessage = null,
            descriptionErrorMessage = null,
            categoryErrorMessage = null
        )
        updateAddBillFormState(newState)

        val filteredValue = validateValue(value, newState)
        if(filteredValue != null) {
            if(description.isEmpty()) {
                updateAddBillFormState(
                    newState.copy(
                        descriptionErrorMessage = "Informe a descrição do valor.",
                        isLoading = false
                    )
                )
                return
            } else if(description.length !in 0..50) {
                updateAddBillFormState(
                    newState.copy(
                        descriptionErrorMessage = "A descrição deve conter de 0 a 50 caracteres.",
                        isLoading = false
                    )
                )
                return
            } else if(addBillFormState.value.category == "Selecione uma categoria") {
                updateAddBillFormState(
                    newState.copy(
                        categoryErrorMessage = "Você deve informar uma categoria.",
                        isLoading = false
                    )
                )
                return
            }

            _addBillFormState.value = _addBillFormState.value.copy(isLoading = true)

            val formattedDate = parseDateFromString(date)
            if(formattedDate != null) {
                val currentUser = auth.currentUser

                currentUser?.let { user ->
                    val newBill = Bill(
                        userId = user.uid,
                        id = UUID.randomUUID().toString().replace("-", ""),
                        //type = addBillFormState.value.type,
                        description = addBillFormState.value.description,
                        billCategory = BillCategory(
                            type = addBillFormState.value.type,
                            name = addBillFormState.value.category
                        ),
                        value = filteredValue,
                        installment = addBillFormState.value.installment,
                        date = formattedDate,
                    )

                    saveBillFirestore(newBill, user.uid)
                }
            } else {
                _addBillFormState.value = _addBillFormState.value.copy(isLoading = false)
            }
        }
    }

    private fun setUpdateMessage(message: String) {
        _updateMessage.value = message
    }

    private fun saveBillFirestore(newBill: Bill, userUid: String) {
        setUpdateMessage("")
        val userDocRef = firestore.collection("users").document(userUid)

        firestore.runTransaction { transaction ->
            val userDoc = transaction.get(userDocRef)
            val userBills = userDoc.get("bills") as? MutableList<HashMap<String, Any>> ?: mutableListOf()

            val newBillMap = hashMapOf<String, Any>(
                "id" to newBill.id,
                "userId" to newBill.userId,
                //"type" to newBill.type,
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
            .addOnSuccessListener {
                Log.i(TAG, "Conta (Bill) salva no Firestore com Sucesso!")
                setUpdateMessage("Conta Adicionada com Sucesso!")
                _addBillFormState.value = _addBillFormState.value.copy(isBillRegistered = true)
                setBillAdded(true)
            }
            .addOnFailureListener {
                Log.i(TAG, "ERRO ao SALVAR Conta (Bill) no FIRESTORE.")
                setUpdateMessage("Erro ao Adicionar Conta.")
                try {
                    throw it
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        _addBillFormState.value = _addBillFormState.value.copy(isLoading = false)
    }

    private fun validateValue(value: String, newState: AddBillFormState): Double? {
        var result: Double? = null
        val decimals = listOf("01", "02", "03", "04", "05", "06", "07", "08", "09")

        if(value == "0") {
            result = 0.0
            return result
        } else if(value in decimals) {
            result = "0.$value".toDouble()
            return result
        }

        if(value.isNotBlank() && value.toDoubleOrNull() != null) {
            try {
                val numericValue = value.replace(Regex("[^\\d]"), "")

                val integerPart = numericValue.substring(0, numericValue.length - 2).toInt()
                val decimalPart = numericValue.substring(numericValue.length - 2).toInt()

                result = integerPart + decimalPart.toDouble() / 100
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if(result == null) {
            updateAddBillFormState(newState.copy(valueErrorMessage = "Valor inválido."))
        }

        return result
    }
}