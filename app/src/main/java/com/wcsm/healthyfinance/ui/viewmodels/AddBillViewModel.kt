package com.wcsm.healthyfinance.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.wcsm.healthyfinance.data.model.AddBillFormState
import com.wcsm.healthyfinance.data.repository.BillRepository
import com.wcsm.healthyfinance.ui.util.parseDateFromString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBillViewModel @Inject constructor(
    private val billRepository: BillRepository,
    auth: FirebaseAuth
) : ViewModel() {

    private val TAG = "#FIREBASE_AUTH#"

    private val _addBillFormState = MutableStateFlow(AddBillFormState())
    val addBillFormState = _addBillFormState.asStateFlow()

    private val _updateMessage = MutableStateFlow("")
    val updateMessage = _updateMessage.asStateFlow()

    private val _billAdded = MutableStateFlow(false)
    val billAdded = _billAdded.asStateFlow()

    private val currentUser = auth.currentUser

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
                    setUpdateMessage("")

                    if(currentUser != null) {
                        viewModelScope.launch {
                            billRepository.saveBillFirestore(
                                currentUser = currentUser,
                                addBillFormState = addBillFormState.value,
                                filteredValue = filteredValue,
                                formattedDate = formattedDate
                            )
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
                    }
            } else {
                _addBillFormState.value = _addBillFormState.value.copy(isLoading = false)
            }
        }
    }

    private fun setUpdateMessage(message: String) {
        _updateMessage.value = message
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